// D:
// cd D:\betn\BigMaster\wsp\SEZDesign\script
// scala -J-Xmx5g -cp "D:\betn\BigMaster\wsp\SEZDesign\libs\jsoup-1.9.2.jar;D:\betn\BigMaster\wsp\SEZDesign\libs\json4s-jackson_2.11-3.5.1.jar"
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

  def getPlotFromOMDb(title : String) : String = {
    val OMDbAPIRoot = """http://www.omdbapi.com/?t="""
    val searchTitle = title.replace(" ","+")
    val doc = Jsoup.connect(OMDbAPIRoot + searchTitle).ignoreContentType(true).get.select("body").text
    doc
  }
  def getAllName() : Array[String] = {
    val path = """D:\betn\BigMaster\wsp\data\IMDB\PlotName"""
    val fs = new java.io.File(path).listFiles.map(_.getName)
    fs
  }
  def storePlot(file : String = """D:\betn\BigMaster\wsp\data\IMDB\netFlixPlots.rst""") = {
    val fs = getAllName
    val printer = new java.io.PrintWriter(file)
    val rand = new scala.util.Random
    val baseTime = 5000
    fs.map{
      movie =>
        Thread.sleep((baseTime * (1 + rand.nextDouble)).toInt)
        try { 
          val rst = getPlotFromOMDb(movie)
          println(movie + " : " + rst)
          printer.write( rst + "\n" )
          printer.flush
          // ...
        } catch {
          case e: Exception => println( movie + e.toString)
        }
    }
    printer.close
  }
}

// IMDBPlots.storePlot("""D:\betn\BigMaster\wsp\data\IMDB\netFlixPlotsShell.rst""")
// IMDBPlots.storePlot()