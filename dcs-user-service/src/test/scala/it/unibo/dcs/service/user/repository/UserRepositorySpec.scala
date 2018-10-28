package it.unibo.dcs.service.user.repository

import java.util.Date

import it.unibo.dcs.service.user.Mocks._
import it.unibo.dcs.service.user.model.User
import it.unibo.dcs.service.user.repository.impl.UserRepositoryImpl
import it.unibo.dcs.service.user.request.{CreateUserRequest, GetUserRequest}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{FlatSpec, OneInstancePerTest}
import rx.lang.scala.{Observable, Subscriber}

class UserRepositorySpec extends FlatSpec with MockFactory with OneInstancePerTest {

  val createUserRequest = CreateUserRequest("martynha", "Martina", "Magnani")
  val getUserRequest = GetUserRequest(createUserRequest.username)

  val expectedUser = User(createUserRequest.username, createUserRequest.firstName, createUserRequest.lastName, "", visible = true, new Date())

  val userRepository = new UserRepositoryImpl(userDataStore)

  val subscriber: Subscriber[User] = stub[Subscriber[User]]

  it should "create new user" in {
    //Given
    (userDataStore createUser _) expects createUserRequest returning (Observable just expectedUser)

    //When
    userRepository.createUser(createUserRequest).subscribe(subscriber)

    //Then
    (subscriber onNext _) verify expectedUser once()
    (() => subscriber onCompleted) verify() once()
  }

  it should "get user by username" in {
    //Given
    (userDataStore getUserByUsername _) expects getUserRequest returning (Observable just expectedUser)

    //When
    userRepository.getUserByUsername(getUserRequest).subscribe(subscriber)

    //Then
    (subscriber onNext _) verify expectedUser once()
    (() => subscriber onCompleted) verify() once()
  }

}
