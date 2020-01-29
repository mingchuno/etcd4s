package org.etcd4s.services

import org.etcd4s.pb.authpb.Permission
import org.etcd4s.pb.etcdserverpb._
import org.etcd4s.rpc.AuthRpc

import scala.concurrent.{ExecutionContext, Future}

@deprecated
private[etcd4s] class AuthService(protected val authRpc: AuthRpc) {
  def authEnable(): Future[AuthEnableResponse] = {
    authRpc.authEnable(AuthEnableRequest())
  }

  def authDisable(): Future[AuthDisableResponse] = {
    authRpc.authDisable(AuthDisableRequest())
  }

  def authenticate(name: String, password: String)(
      implicit ec: ExecutionContext
  ): Future[String] = {
    authRpc
      .authenticate(AuthenticateRequest(name = name, password = password))
      .map(_.token)
  }

  def userAdd(name: String, password: String): Future[AuthUserAddResponse] = {
    authRpc.userAdd(AuthUserAddRequest(name = name, password = password))
  }

  def userGet(name: String)(implicit ec: ExecutionContext): Future[Seq[String]] = {
    authRpc.userGet(AuthUserGetRequest(name = name)).map(_.roles)
  }

  def userList()(implicit ec: ExecutionContext): Future[Seq[String]] = {
    authRpc.userList(AuthUserListRequest()).map(_.users)
  }

  def userDelete(name: String): Future[AuthUserDeleteResponse] = {
    authRpc.userDelete(AuthUserDeleteRequest(name = name))
  }

  def userChangePassword(name: String, password: String): Future[AuthUserChangePasswordResponse] = {
    authRpc.userChangePassword(AuthUserChangePasswordRequest(name = name, password = password))
  }

  def userGrantRole(user: String, role: String): Future[AuthUserGrantRoleResponse] = {
    authRpc.userGrantRole(AuthUserGrantRoleRequest(user = user, role = role))
  }

  def userRevokeRole(name: String, role: String): Future[AuthUserRevokeRoleResponse] = {
    authRpc.userRevokeRole(AuthUserRevokeRoleRequest(name = name, role = role))
  }

  def roleAdd(name: String): Future[AuthRoleAddResponse] = {
    authRpc.roleAdd(AuthRoleAddRequest(name = name))
  }

  def roleGet(role: String)(implicit ec: ExecutionContext): Future[Seq[Permission]] = {
    authRpc.roleGet(AuthRoleGetRequest(role)).map(_.perm)
  }

  def roleList()(implicit ec: ExecutionContext): Future[Seq[String]] = {
    authRpc.roleList(AuthRoleListRequest()).map(_.roles)
  }

  def roleDelete(role: String): Future[AuthRoleDeleteResponse] = {
    authRpc.roleDelete(AuthRoleDeleteRequest(role = role))
  }

  def roleGrantPermission(
      role: String,
      perm: Permission
  ): Future[AuthRoleGrantPermissionResponse] = {
    authRpc.roleGrantPermission(AuthRoleGrantPermissionRequest(role, Some(perm)))
  }

  def roleRevokePermission(
      role: String,
      key: String,
      rangeEnd: String
  ): Future[AuthRoleRevokePermissionResponse] = {
    authRpc.roleRevokePermission(
      AuthRoleRevokePermissionRequest(role = role, key = key, rangeEnd = rangeEnd)
    )
  }
}
