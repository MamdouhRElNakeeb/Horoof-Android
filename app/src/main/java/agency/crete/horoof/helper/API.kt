package agency.crete.horoof.helper

open class API {

    companion object {

        const val IP = "68.183.96.88:8080"
        const val SERVER = "http://$IP"
        const val WS = "ws://$IP"

        private const val API = "$SERVER/api"
        const val LOGIN = "$API/authenticate"
        const val REGISTER = "$API/register"

        const val CATEGORIES = "$API/categories"
        const val CATEGORY = "$API/category"
        const val QUESTIONS = "$API/questions"
        const val COMPETITIONS = "$API/competitions"


        const val TRACKER = "$WS/websocket/tracker/websocket"
        const val COMPETITION = ""
        const val ACTIVITY = ""
    }
}