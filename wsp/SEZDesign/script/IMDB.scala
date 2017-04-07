// D:
// cd D:\betn\BigMaster\wsp\SEZDesign\script
// scala -J-Xmx5g -cp "D:\betn\BigMaster\wsp\SEZDesign\libs\jsoup-1.9.2.jar;D:\betn\BigMaster\wsp\SEZDesign\libs\json4s-jackson_2.11-3.5.1.jar" IMDB.scala 
object IMDBPlotsRun extends App {
  import org.jsoup._
  import scala.collection.JavaConverters._
  import collection.mutable.Buffer
  import scala.collection.mutable.Buffer

  val AvengersTargetUrl = """http://www.imdb.com/search/text?realm=title&field=plot&q=The+Avengers"""
  val avengerTitle = """The Avengers"""
  val rzdfTitle = """Rang zi dan fei"""
  val rootUrl = """http://www.imdb.com"""

  def urlGet( _url : String , _timeOut : Int = 1000*60*1 ) = Jsoup.connect(_url).timeout(_timeOut).get

  def genUrl(title : String) : String = {
    val searchUrl = """http://www.imdb.com/find?ref_=nv_sr_fn&q="""
    val targetTitle = title.replace(" ", "+")
    val urlTail = """&s=all"""
    val targetUrl = searchUrl + targetTitle + urlTail
    targetUrl  
  }

  def getPlotsByUrl(url : String) : Array[String] = {
    val plotDoc = urlGet(url)
    val plotSummary = plotDoc.select("p.plotSummary")
    val plotIter = plotSummary.iterator
    val plots = new Array[String](plotSummary.size)
    plots.map(x => plotIter.next.text)
  }

  def getPlotsByTitle(title : String) : Buffer[Array[String]] = {
    val targetUrl = genUrl(title)
    val doc = urlGet(targetUrl)
    val resultText = doc.select("td.result_text")
    val bufferHref : Buffer[String] = resultText.asScala.map{
      elem =>
        elem.select("a").first.attributes.get("href")
    }.filter(_.startsWith("/title")).map(x => rootUrl + x)
    val movieDocs = bufferHref.map(urlGet(_))
    val plotDocs : Buffer[String] = movieDocs.map{
      movieDoc =>
        val movieSelectArr = movieDoc
          .select("a").asScala.filter(_.text == "Plot Summary")
        val moviePlotSummary = (movieSelectArr.size > 0) match{
          case true => movieSelectArr.head.attributes.get("href")
          case false => ""
        }
        val quicklinkSectionArr = movieDoc
          .getElementsByClass("quicklinkSectionItem")
          .asScala.filter(_.text == "Plot Summary")
        val quicklinkSectionItems = 
          (quicklinkSectionArr.size > 0) match {
            case true => quicklinkSectionArr
              .head.select("a").first
              .attributes.get("href")
            case false => ""
          }
        val plotUrl = if(quicklinkSectionItems.isEmpty) 
          moviePlotSummary
          else quicklinkSectionItems
        rootUrl + plotUrl
    }
    plotDocs.filter(_.size > 0).map(x => getPlotsByUrl(x))
  }
  // def getPlotsByTitle(title : String) = 
  //   getPlotsUrlByTitle(title).map(getPlotsByUrl)
  // val doc = getPlotsByTitle(rzdfTitle)

  def getPlotFromOMDb(movie : Array[String], _timeOut : Int = 1000*60*2 ) : String = {
    val OMDbAPIRoot = """http://www.omdbapi.com/?t="""
    val years = movie(1)
    val title = movie(2)
    val searchTitle = title.replace("%","%25").replace(":","%3A").replace(" ","+")
    val searchURL = s"""http://www.omdbapi.com/?t=${searchTitle}&y=${years}&plot=full"""
    val doc = Jsoup.connect(searchURL).timeout(_timeOut).ignoreContentType(true).get.select("body").text
    doc
  }
  def getAllName() : Array[Array[String]] = {
    val path = """D:\betn\BigMaster\wsp\data\netflixRst\movie_titles.txt"""
    scala.io.Source.fromFile(path).getLines.
      map(_.split(',')).toArray
    }
  def storePlot(file : String = """D:\betn\BigMaster\wsp\data\IMDB\netFlixPlotsFull.rst""") = {
    val fs = getAllName
    val printer = new java.io.PrintWriter(file)
    val rand = new scala.util.Random
    val baseTime = 100
    val buf = new scala.collection.mutable.ArrayBuffer[Array[String]](fs.size)
    fs.filter(_.size > 0).map( buf += _ )
    // (0 to 10).map{
    var count = 11
    var remainCount = buf.size
    while(buf.size > 0 && count > 0) {
      try { 
        println( s"INFO : fs.map started! remain buf.size = ${buf.size}" )
        if(remainCount == buf.size) count -= 1
        else remainCount = buf.size

        buf.map{
          movie =>
            Thread.sleep((baseTime * (1 + rand.nextDouble)).toInt)
              println("INFO : " + movie(2) + " : Doing!")
              val rst = getPlotFromOMDb(movie)
              println("INFO : " + movie(2) + " : " + rst + " : Done!")
              printer.write( rst + "\n" )
              printer.flush
              buf -= movie
        }
              // ...
      } catch {
        case e: Exception => println( "ERROR : " + " : " + e.toString)
      }
      println( s"INFO : fs.map finished! remain buf.size = ${buf.size}" )

    }
    // }
    printer.flush
    printer.close
  }
}

IMDBPlotsRun.storePlot("""D:\betn\BigMaster\wsp\data\IMDB\netFlixPlotsFullAll.rst""")
// IMDBPlots.storePlot()
