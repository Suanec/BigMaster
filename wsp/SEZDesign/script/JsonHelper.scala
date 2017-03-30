///author := "suanec_Betn"
///data := 20160918
// scala -cp "D:\betn\BigMaster\wsp\SEZDesign\libs\jsoup-1.9.2.jar;D:\betn\BigMaster\wsp\SEZDesign\libs\json4s-jackson_2.11-3.5.1.jar"

object imdbJsonHelper{
  import org.json4s._
  import org.json4s.jackson._
  import org.json4s.jackson.JsonMethods._
  import org.json4s.JsonDSL._
  import org.json4s.JsonDSL.map2jvalue
  // or
  //import org.json4s.JsonDSL.WithDouble._
  //import org.json4s.JsonDSL.WithBigDecimal._
  //import scala.io.Source
  //import scala.collection.JavaConverters._
  //import scala.collection.mutable.Buffer
  import java.io.{File,PrintWriter}
  import java.util.Date
  implicit val formats = Serialization.formats(ShortTypeHints(List()))
  val filePath = """D:\betn\BigMaster\wsp\data\IMDB\plots5k.rst"""
  /// read Config from file name
  def readConf( _file : String = "" ) : Array[JValue] = {
    var file = _file
    val conf = io.Source.fromFile(file).mkString
    conf.split('\n').map(x => parse(x))
  }
  def imdbRstToPairTitilPlotsJson(file : String = filePath) : String = {
    val data = readConf(file)
    val t = data.head
    val r = "Response"
    val n = "Title"
    val p = "Plot"
    val contents = data.filter( row => (row \ r).extract[String].toBoolean).filter(row => (row \ p).extract[String] != "N/A")
    val plots = contents.map{
      row =>
        val title = (row \ n).values.toString
        val plots = (row \ p)
        title -> plots
    }
    // val plotsIter = plots.iterator
    // var rstObj = plotsIter.next ~ plotsIter.next
    // while(plotsIter.hasNext) rstObj = plotsIter.next ~ rstObj
    val (head,tail) = plots.splitAt(2)
    val rstObj = tail.foldRight(head.head ~ head.last)(_ ~ _)
    val rstObj = tail.aggregate(head.head ~ head.last)(_ ~ _, _ ~ _)
    val rstString = pretty(rstObj)
    rstString
  }
}  

// object weiJsonHelper{
//   implicit val formats = Serialization.formats(ShortTypeHints(List()))
//   def genDataArray( _listArr : Array[String]) : Array[(String,Array[String])] = {
//     val ids = findIdIndex(_listArr)
//     val rst = new Array[(String,Array[String])](ids.size)
//     val featureArr = new Array[String](ids(1)-ids(0)-1)
//     ids.indices.map{
//       i =>
//         featureArr.indices.map{
//           j =>
//             featureArr(j) = _listArr(j+ids(i)+1)
//         }
//         rst(i) = (_listArr(ids(i)),featureArr.clone)
//     }
//     rst
//   }

//   /// list data to Map, call functions objs' operation ; 
//   /// _listStr is a string that all info one line 
//   def list2Map( _listStr : String ) : Map[String,Map[String,String]] = {
//     val arr = _listStr.split('\n').tail
//     val ids = findIdIndex(arr)
//     val rst = arr2ObjByIds( arr, ids )
//     rst
//   }
//   /// list data to json
//   /// _listStr is a string that all info one line 
//   def list2Json( _listRst : Map[String,Map[String,String]], _conf : String ) : String = {
//     val rst1 = parse(_conf)
//     val rst2 = render(("ApplicationInfo",_listRst))
//     val rst = pretty(render(rst1 merge rst2))
//     rst
//   } 
//   /// grammar sugar
//   def listStr2Json( _listStr : String, _conf : String ) : String = {
//     val rst = pretty(
//       render(
//         parse(_conf) merge render(
//           ( "ApplicationInfo",list2Map(_listStr) 
//           )
//         )
//       )
//     )
//     rst
//   }
//   /// info to json
//   /// info is a Array(applicationId,Array[appInfo])
//   def info2Json0( _arrData : Array[(String,Array[String])], _conf : String ) : String = {
//     val data = _arrData.map{
//       k =>
//         val kk = k._2.map{
//           l =>
//             val split = l.split(':')
//             (split.head,split.last)
//         }.toMap
//       (k._1,kk)
//     }.toMap
//     val d = new Date(System.currentTimeMillis).toString
//     val dd = "Date" -> d
//     val rst2 = render(data.toList)
//     val rst1 = dd ++ parse(_conf)
//     pretty(render(rst1 merge rst2))
//   }
//   /// MonitorSparkUI rstArr to Map[String,String]
//   def uiArr2JObject0( _arrData : Array[(String, Array[String])] ) : JValue = {
//     val ids = _arrData.map(_._1)
//     val objs = ids.indices.map{
//       i =>
//         val str = "JobId: " + ids(i)
//         (Array(str) ++ _arrData(i)._2).map{
//           k =>
//             val kk = k.split(':')
//             (kk.head,kk.last)
//         }.toMap
//     }
//     var sumObj = render(objs.head)
//     (1 until objs.size).map{
//       i =>
//         sumObj = sumObj ++ render(objs(i))
//     }
//     sumObj
//   }
//   def uiArr2JObject( _arrData : Array[(String, Array[String])] ) : JArray = {
//     val ids = _arrData.map(_._1)
//     val objs = ids.indices.map{
//       i =>
//         val str = "JobId: " + ids(i)
//         (Array(str) ++ _arrData(i)._2).map{
//           k =>
//             val kk = k.split(':')
//             (kk.head,kk.last)
//         }.toMap
//     }.map( render(_) ).toList
//     JArray(objs)
//   }
//   def info2Json( _arrData : Array[(String,Array[String])], _conf : String ) : String = {
//     val sumObj = uiArr2JObject(_arrData)
//     val confObj = parse(_conf)
//     pretty( ("conf",confObj) ~ ("ApplicationInfo",sumObj) )
//   }
// }  

