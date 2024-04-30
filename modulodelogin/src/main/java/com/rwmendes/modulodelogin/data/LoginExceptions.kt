package com.rwmendes.modulodelogin.data

class CredentialsIncorrectException(message: String) : Exception(message)

class UserNotFoundException(message: String) : Exception(message)

class NetworkErrorException(message: String) : Exception(message)
