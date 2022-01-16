package ua.leonidius.garage.service.car_detail.request_handlers

abstract class BaseHandler: RequestHandler {

    private var nextHandler: RequestHandler? = null

    override fun getNext() = nextHandler

    override fun setNext(handler: RequestHandler) {
        nextHandler = handler
    }

}