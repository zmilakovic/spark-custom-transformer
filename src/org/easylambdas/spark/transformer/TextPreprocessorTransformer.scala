package org.easylambdas.spark.transformer

import org.apache.spark.ml.UnaryTransformer
import org.apache.spark.ml.util.Identifiable
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.types.StringType
import java.util.Properties
import edu.stanford.nlp.pipeline.StanfordCoreNLP
import edu.stanford.nlp.pipeline.Annotation
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation
import scala.collection.JavaConversions._
import edu.stanford.nlp.ling.CoreAnnotations.LemmaAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation
import org.apache.spark.sql.types.ArrayType
import org.apache.spark.sql.types.DataTypes

/**
 * Companion object to hold static objects which can't be serialized.
 * Keeping those objects in the companion objects prevents re-initialization on each method call
 */
object TextPreprocessorTransformer {

  val minTokenLength = 2
  val props = new Properties()
  props.put("annotators", "tokenize, ssplit, pos, lemma")
  val pipeline = new StanfordCoreNLP(props)
  val charset = "iso-8859-1"

  /**
   * Transforms words to a single string using a one space between words
   */
  def wordsToText(words: Seq[String]): String = {
    words.foldLeft("")((x, y) => x.trim() + " " + y)
  }

}

class TextPreprocessorTransformer(override val uid: String)
    extends UnaryTransformer[String, Seq[String], TextPreprocessorTransformer] {

  def this() = this(Identifiable.randomUID("TextPreprocessorTransformer"))

  override protected def validateInputType(inputType: DataType): Unit = {
    require(inputType == StringType)
  }

  protected def outputDataType: DataType = new ArrayType(StringType, true)

  //load stopwords from Stopwords module
  val stopwordsSet = loadWordList("/resources/stopwords.txt").get

  //load English words
  val englishWordsSet = loadWordList("/resources/english_words.txt").get

  def processText(text: String): Seq[String] = {
    val doc = new Annotation(text.toLowerCase)

    TextPreprocessorTransformer.pipeline.annotate(doc)
    val sentences = doc.get(classOf[SentencesAnnotation])
    val lemmas = sentences
      .flatMap { sentence => sentence.get(classOf[TokensAnnotation]) }
      .filter { token => token.originalText.length > TextPreprocessorTransformer.minTokenLength && !stopwordsSet.contains(token.originalText) }
      //&& englishWordsSet.contains(token.originalText)
      .map { token => token.lemma }
    lemmas
  }

  /**
   * Loads standard stop word list from the classpath and the file named "stopwords.txt"
   * Loads standard English words list from the classpath and the file named "english_words.txt"
   */
  def loadWordList(path: String): Option[Set[String]] = {
    val is = getClass.getResourceAsStream(path)
    try {
      if (is != null) {
        Some(scala.io.Source.fromInputStream(is, TextPreprocessorTransformer.charset)
          .getLines.map { x => x.trim().toLowerCase }.toSet)
      } else {
        None
      }
    } finally {
      if (is != null) is.close()
    }
  }

  /**  Override function for creation of the transform function  */
  protected def createTransformFunc: String => Seq[String] = {
    text => this.processText(text)
  }

}


