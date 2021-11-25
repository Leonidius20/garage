package ua.leonidius.garage.business.request_handlers

abstract class BaseHandler: RequestHandler {

    private var nextHandler: RequestHandler? = null

    override fun getNext() = nextHandler

    override fun setNext(handler: RequestHandler) {
        nextHandler = handler
    }

}