package pw.androidthanatos.blog.common.util

import com.google.gson.*

/**
 * 自定义　JSONObject
 */
class JSONObject{

    private var json = JsonObject()

    constructor()
    constructor(obj: JsonObject){
        this.json = obj
    }
    constructor(obj: String){
        this.json = JsonParser().parse(obj).asJsonObject
    }

    fun putString(key: String, value: String): JSONObject{
        json.addProperty(key, value)
        return this
    }

    fun putNumber(key: String, value: Number): JSONObject{
        json.addProperty(key, value)
        return this
    }

    fun putBool(key: String, value: Boolean): JSONObject{
        json.addProperty(key, value)
        return this
    }

    fun putElement(key: String, value: JsonElement): JSONObject{
        json.add(key, value)
        return this
    }

    fun getString(key: String, def: String = ""): String{
        return json.get(key).asString?:def
    }

    fun getNumber(key: String, def: Number = 0): Number{
        return json.get(key).asNumber?:def
    }

    fun getBool(key: String, def: Boolean = false): Boolean{
        return json.get(key).asBoolean?:def
    }

    fun getJSONArray(key: String, def: JSONArray = JSONArray()): JSONArray{
        val array = json.getAsJsonArray(key)
        return if (null == array){
            def
        }else{
            JSONArray(array)
        }
    }

    override fun toString(): String{
        return json.toString()
    }


    fun toMap(): Map<String, JsonElement>{
        val map = HashMap<String, JsonElement>()
        json.keySet().forEach {
            map[it] = json.get(it)
        }
        return map
    }

    fun forEach(next:(String, JsonElement)->Unit){
        toMap().forEach {
            next.invoke(it.key, it.value)
        }
    }

    fun size(): Int{
        return toMap().size
    }

    fun isEmpty(): Boolean = toMap().isEmpty()
}

/**
 * 自定义　JSONArray
 */
class JSONArray{

    private var array = JsonArray()

    constructor()
    constructor(array: JsonArray){
        this.array = array
    }
    constructor(array: String){
        this.array = JsonParser().parse(array).asJsonArray
    }


    fun putString(value: String): JSONArray{
        array.add(value)
        return this
    }

    fun putNumber(value: Number): JSONArray{
        array.add(value)
        return this
    }

    fun putBool(value: Boolean): JSONArray{
        array.add(value)
        return this
    }

    fun putArray(value: JSONArray): JSONArray{
        array.add(value.array)
        return this
    }

    fun putObject(value: JSONObject): JSONArray{
        array.add(Gson().toJsonTree(value.toString()))
        return this
    }

    fun forEach(next:(JsonElement)->Unit){
        array.forEach {
            next.invoke(it)
        }
    }

    fun size(): Int{
        return toArray().size
    }

    fun isEmpty(): Boolean = toArray().isEmpty()

    override fun toString(): String {
        return array.toString()
    }

    fun toArray(): List<*>{
        return array.toList()
    }
}