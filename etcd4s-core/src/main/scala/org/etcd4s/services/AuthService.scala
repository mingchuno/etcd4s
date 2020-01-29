package org.etcd4s.services

import org.etcd4s.pb.authpb.Permission
import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.rpc.AuthRpc

import scala.concurrent.{ExecutionContext, Future}

trait AuthService {
  val authApi: AuthRpc
  def authEnable(): Future[AuthEnableResponse] = {
    authApi.authEnable(AuthEnableRequest())
  }

  def authDisable(): Future[AuthDisableResponse] = {
    authApi.authDisable(AuthDisableRequest())
  }

  def authenticate(name: String, password: String)(
      implicit ec: ExecutionContext
  ): Future[String] = {
    authApi
      .authenticate(AuthenticateRequest(name = name, password = password))
      .map(_.token)
  }

  def userAdd(name: String, password: String): Future[AuthUserAddResponse] = {
    authApi.userAdd(AuthUserAddRequest(name = name, password = password))
  }

  def userGet(name: String)(implicit ec: ExecutionContext): Future[Seq[String]] = {
    authApi.userGet(AuthUserGetRequest(name = name)).map(_.roles)
  }

  def userList()(implicit ec: ExecutionContext): Future[Seq[String]] = {
    authApi.userList(AuthUserListRequest()).map(_.users)
  }

  def userDelete(name: String): Future[AuthUserDeleteResponse] = {
    authApi.userDelete(AuthUserDeleteRequest(name = name))
  }

  def userChangePassword(name: String, password: String): Future[AuthUserChangePasswordResponse] = {
    authApi.userChangePassword(AuthUserChangePasswordRequest(name = name, password = password))
  }

  def userGrantRole(user: String, role: String): Future[AuthUserGrantRoleResponse] = {
    authApi.userGrantRole(AuthUserGrantRoleRequest(user = user, role = role))
  }

  def userRevokeRole(name: String, role: String): Future[AuthUserRevokeRoleResponse] = {
    authApi.userRevokeRole(AuthUserRevokeRoleRequest(name = name, role = role))
  }

  def roleAdd(name: String): Future[AuthRoleAddResponse] = {
    authApi.roleAdd(AuthRoleAddRequest(name = name))
  }

  def roleGet(role: String)(implicit ec: ExecutionContext): Future[Seq[Permission]] = {
    authApi.roleGet(AuthRoleGetRequest(role)).map(_.perm)
  }

  def roleList()(implicit ec: ExecutionContext): Future[Seq[String]] = {
    authApi.roleList(AuthRoleListRequest()).map(_.roles)
  }

  def roleDelete(role: String): Future[AuthRoleDeleteResponse] = {
    authApi.roleDelete(AuthRoleDeleteRequest(role = role))
  }

  def roleGrantPermission(
      role: String,
      perm: Permission
  ): Future[AuthRoleGrantPermissionResponse] = {
    authApi.roleGrantPermission(AuthRoleGrantPermissionRequest(role, Some(perm)))
  }

  def roleRevokePermission(
      role: String,
      key: String,
      rangeEnd: String
  ): Future[AuthRoleRevokePermissionResponse] = {
    authApi.roleRevokePermission(
      AuthRoleRevokePermissionRequest(role = role, key = key, rangeEnd = rangeEnd)
    )
  }
}
