package org.etcd4s.services

import org.etcd4s.pb.authpb.Permission
import org.etcd4s.pb.etcdserverpb.AuthGrpc.{Auth, AuthStub}
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.{ExecutionContext, Future}

private[etcd4s] class AuthService(protected val stub: AuthStub) extends Auth {

  override def authEnable(request: AuthEnableRequest) = stub.authEnable(request)

  def authEnable(): Future[AuthEnableResponse] = {
    authEnable(AuthEnableRequest())
  }

  override def authDisable(request: AuthDisableRequest) = stub.authDisable(request)

  def authDisable(): Future[AuthDisableResponse] = {
    authDisable(AuthDisableRequest())
  }

  override def authenticate(request: AuthenticateRequest) = stub.authenticate(request)

  def authenticate(name: String, password: String)(implicit ec: ExecutionContext): Future[String] = {
    authenticate(AuthenticateRequest(name = name, password = password))
      .map(_.token)
  }

  override def userAdd(request: AuthUserAddRequest) = stub.userAdd(request)

  def userAdd(name: String, password: String): Future[AuthUserAddResponse] = {
    userAdd(AuthUserAddRequest(name = name, password = password))
  }

  override def userGet(request: AuthUserGetRequest) = stub.userGet(request)

  def userGet(name: String)(implicit ec: ExecutionContext): Future[Seq[String]] = {
    userGet(AuthUserGetRequest(name = name)).map(_.roles)
  }

  override def userList(request: AuthUserListRequest) = stub.userList(request)

  def userList()(implicit ec: ExecutionContext): Future[Seq[String]] = {
    userList(AuthUserListRequest()).map(_.users)
  }

  override def userDelete(request: AuthUserDeleteRequest) = stub.userDelete(request)

  def userDelete(name: String): Future[AuthUserDeleteResponse] = {
    userDelete(AuthUserDeleteRequest(name = name))
  }

  override def userChangePassword(request: AuthUserChangePasswordRequest) = stub.userChangePassword(request)

  def userChangePassword(name: String, password: String): Future[AuthUserChangePasswordResponse] = {
    userChangePassword(AuthUserChangePasswordRequest(name = name, password = password))
  }

  override def userGrantRole(request: AuthUserGrantRoleRequest) = stub.userGrantRole(request)

  def userGrantRole(user: String, role: String): Future[AuthUserGrantRoleResponse] = {
    userGrantRole(AuthUserGrantRoleRequest(user = user, role = role))
  }

  override def userRevokeRole(request: AuthUserRevokeRoleRequest) = stub.userRevokeRole(request)

  def userRevokeRole(name: String, role: String): Future[AuthUserRevokeRoleResponse] = {
    userRevokeRole(AuthUserRevokeRoleRequest(name = name, role = role))
  }

  override def roleAdd(request: AuthRoleAddRequest) = stub.roleAdd(request)

  def roleAdd(name: String): Future[AuthRoleAddResponse] = {
    roleAdd(AuthRoleAddRequest(name = name))
  }

  override def roleGet(request: AuthRoleGetRequest) = stub.roleGet(request)

  def roleGet(role: String)(implicit ec: ExecutionContext): Future[Seq[Permission]] = {
    roleGet(AuthRoleGetRequest(role)).map(_.perm)
  }

  override def roleList(request: AuthRoleListRequest) = stub.roleList(request)

  def roleList()(implicit ec: ExecutionContext): Future[Seq[String]] = {
    roleList(AuthRoleListRequest()).map(_.roles)
  }

  override def roleDelete(request: AuthRoleDeleteRequest) = stub.roleDelete(request)

  def roleDelete(role: String): Future[AuthRoleDeleteResponse] = {
    roleDelete(AuthRoleDeleteRequest(role = role))
  }

  override def roleGrantPermission(request: AuthRoleGrantPermissionRequest) = stub.roleGrantPermission(request)

  def roleGrantPermission(role: String, perm: Permission): Future[AuthRoleGrantPermissionResponse] = {
    roleGrantPermission(AuthRoleGrantPermissionRequest(role, Some(perm)))
  }

  override def roleRevokePermission(request: AuthRoleRevokePermissionRequest) = stub.roleRevokePermission(request)

  def roleRevokePermission(role: String, key: String, rangeEnd: String): Future[AuthRoleRevokePermissionResponse] = {
    roleRevokePermission(AuthRoleRevokePermissionRequest(role = role, key = key, rangeEnd = rangeEnd))
  }

}
