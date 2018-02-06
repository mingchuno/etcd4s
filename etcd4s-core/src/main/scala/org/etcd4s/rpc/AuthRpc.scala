package org.etcd4s.rpc

import org.etcd4s.pb.etcdserverpb.AuthGrpc.{Auth, AuthStub}
import org.etcd4s.pb.etcdserverpb._

private[etcd4s] class AuthRpc(protected val stub: AuthStub) extends Auth {

  override def authEnable(request: AuthEnableRequest) = stub.authEnable(request)

  override def authDisable(request: AuthDisableRequest) = stub.authDisable(request)

  override def authenticate(request: AuthenticateRequest) = stub.authenticate(request)

  override def userAdd(request: AuthUserAddRequest) = stub.userAdd(request)

  override def userGet(request: AuthUserGetRequest) = stub.userGet(request)

  override def userList(request: AuthUserListRequest) = stub.userList(request)

  override def userDelete(request: AuthUserDeleteRequest) = stub.userDelete(request)

  override def userChangePassword(request: AuthUserChangePasswordRequest) = stub.userChangePassword(request)

  override def userGrantRole(request: AuthUserGrantRoleRequest) = stub.userGrantRole(request)

  override def userRevokeRole(request: AuthUserRevokeRoleRequest) = stub.userRevokeRole(request)

  override def roleAdd(request: AuthRoleAddRequest) = stub.roleAdd(request)

  override def roleGet(request: AuthRoleGetRequest) = stub.roleGet(request)

  override def roleList(request: AuthRoleListRequest) = stub.roleList(request)

  override def roleDelete(request: AuthRoleDeleteRequest) = stub.roleDelete(request)

  override def roleGrantPermission(request: AuthRoleGrantPermissionRequest) = stub.roleGrantPermission(request)

  override def roleRevokePermission(request: AuthRoleRevokePermissionRequest) = stub.roleRevokePermission(request)

}
