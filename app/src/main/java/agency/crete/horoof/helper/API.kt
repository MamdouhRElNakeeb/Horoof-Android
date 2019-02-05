package agency.crete.horoof.helper

open class API {

    companion object {
        const val SERVER = "http://35.207.65.83:8080"
        const val LOGIN = "$SERVER/v1/competitor/auth/signin"
        const val REGISTER = "$SERVER/v1/competitor/auth/register"

        const val CATEGORIES = "$SERVER/v1/competitor/category"
        const val QUESTIONS = "/questions"
    }

}