package org.etcd4s.rpc

import org.etcd4s.pb.etcdserverpb.AuthGrpc.{Auth, AuthStub}
import org.etcd4s.pb.etcdserverpb._

import scala.concurrent.Future

private[etcd4s] class AuthRpc(protected val stub: AuthStub) extends Auth {

  override def authEnable(request: AuthEnableRequest): Future[AuthEnableResponse] = stub.authEnable(request)

  override def authDisable(request: AuthDisableRequest): Future[AuthDisableResponse] = stub.authDisable(request)

  override def authenticate(request: AuthenticateRequest): Future[AuthenticateResponse] = stub.authenticate(request)

  override def userAdd(request: AuthUserAddRequest): Future[AuthUserAddResponse] = stub.userAdd(request)

  override def userGet(request: AuthUserGetRequest): Future[AuthUserGetResponse] = stub.userGet(request)

  override def userList(request: AuthUserListRequest): Future[AuthUserListResponse] = stub.userList(request)

  override def userDelete(request: AuthUserDeleteRequest): Future[AuthUserDeleteResponse] = stub.userDelete(request)

  override def userChangePassword(request: AuthUserChangePasswordRequest): Future[AuthUserChangePasswordResponse] =
    stub.userChangePassword(request)

  override def userGrantRole(request: AuthUserGrantRoleRequest): Future[AuthUserGrantRoleResponse] = stub.userGrantRole(request)

  override def userRevokeRole(request: AuthUserRevokeRoleRequest): Future[AuthUserRevokeRoleResponse] = stub.userRevokeRole(request)

  override def roleAdd(request: AuthRoleAddRequest): Future[AuthRoleAddResponse] = stub.roleAdd(request)

  override def roleGet(request: AuthRoleGetRequest): Future[AuthRoleGetResponse] = stub.roleGet(request)

  override def roleList(request: AuthRoleListRequest): Future[AuthRoleListResponse] = stub.roleList(request)

  override def roleDelete(request: AuthRoleDeleteRequest): Future[AuthRoleDeleteResponse] = stub.roleDelete(request)

  override def roleGrantPermission(request: AuthRoleGrantPermissionRequest): Future[AuthRoleGrantPermissionResponse] =
    stub.roleGrantPermission(request)

  override def roleRevokePermission(request: AuthRoleRevokePermissionRequest): Future[AuthRoleRevokePermissionResponse] =
    stub.roleRevokePermission(request)

}
