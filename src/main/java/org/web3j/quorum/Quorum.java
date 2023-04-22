/*
 * Copyright 2019 Web3 Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.web3j.quorum;

import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.quorum.methods.request.*;
import org.web3j.quorum.methods.response.*;
import org.web3j.quorum.methods.response.extension.*;
import org.web3j.quorum.methods.response.istanbul.IstanbulBlockSigners;
import org.web3j.quorum.methods.response.istanbul.IstanbulCandidates;
import org.web3j.quorum.methods.response.istanbul.IstanbulNodeAddress;
import org.web3j.quorum.methods.response.istanbul.IstanbulSnapshot;
import org.web3j.quorum.methods.response.istanbul.IstanbulValidators;
import org.web3j.quorum.methods.response.permissioning.ExecStatusInfo;
import org.web3j.quorum.methods.response.permissioning.OrgDetailsInfo;
import org.web3j.quorum.methods.response.permissioning.PermissionAccountList;
import org.web3j.quorum.methods.response.permissioning.PermissionNodeList;
import org.web3j.quorum.methods.response.permissioning.PermissionOrgList;
import org.web3j.quorum.methods.response.permissioning.PermissionRoleList;
import org.web3j.quorum.methods.response.raft.RaftCluster;
import org.web3j.quorum.methods.response.raft.RaftLeader;
import org.web3j.quorum.methods.response.raft.RaftPeerId;
import org.web3j.quorum.methods.response.raft.RaftPromote;
import org.web3j.quorum.methods.response.raft.RaftRole;

/** JSON-RPC Request object building factory for Quorum. */
public interface Quorum extends Web3j {
    static Quorum build(Web3jService web3jService) {
        return new JsonRpc2_0Quorum(web3jService);
    }

    Request<?, EthSendTransaction> ethSendTransaction(PrivateTransaction transaction);

    Request<?, PrivatePayload> quorumGetPrivatePayload(String hexDigest);

    Request<?, ContractPrivacyMetadataInfo> quorumGetContractPrivacyMetadata(String hexDigest);

    Request<?, EthSendTransaction> ethSendRawTransaction(String signedTransactionData);

    default Request<?, EthSendTransaction> ethSendRawPrivateTransaction(
            String signedTransactionData, List<String> privateFor) {
        return ethSendRawPrivateTransaction(signedTransactionData, privateFor, null, null);
    }

    Request<?, EthSendTransaction> ethSendRawPrivateTransaction(
            String signedTransactionData, List<String> privateFor, PrivacyFlag privacyFlag);

    Request<?, EthSendTransaction> ethSendRawPrivateTransaction(
            String signedTransactionData,
            List<String> privateFor,
            PrivacyFlag privacyFlag,
            List<String> mandatoryFor);

    Request<?, EthSendTransaction> ethSendTransactionAsync(PrivateTransaction transaction);

    Request<?, EthGetQuorumTransactionReceipt> ethGetQuorumTransactionReceipt(String hexDigest);

    // privacy marker transactions

    Request<?, EthSendTransaction> ethDistributePrivateTransaction(
            String signedTransactionData, List<String> privateFor);

    Request<?, EthSendTransaction> ethDistributePrivateTransaction(
            String signedTransactionData, List<String> privateFor, PrivacyFlag privacyFlag);

    Request<?, EthSendTransaction> ethDistributePrivateTransaction(
            String signedTransactionData,
            List<String> privateFor,
            PrivacyFlag privacyFlag,
            List<String> mandatoryFor);

    Request<?, EthAddress> ethGetPrivacyPrecompileAddress();

    Request<?, EthTransaction> ethGetPrivateTransactionByHash(String hexDigest);

    Request<?, EthGetTransactionReceipt> ethGetPrivateTransactionReceipt(String hexDigest);

    // raft consensus

    Request<?, RaftLeader> raftGetLeader();

    Request<?, RaftRole> raftGetRole();

    Request<?, RaftPeerId> raftAddPeer(String enode);

    Request<?, ConsensusNoResponse> raftRemovePeer(int peerId);

    Request<?, RaftCluster> raftGetCluster();

    Request<?, RaftPeerId> raftAddLearner(String enode);

    Request<?, RaftPromote> raftPromoteToPeer(int raftId);

    // istanbul consensus

    Request<?, IstanbulSnapshot> istanbulGetSnapshot(String blockNum);

    Request<?, IstanbulSnapshot> istanbulGetSnapshotAtHash(String blockHash);

    Request<?, IstanbulValidators> istanbulGetValidators(String blockNum);

    Request<?, IstanbulValidators> istanbulGetValidatorsAtHash(String blockHash);

    Request<?, ConsensusNoResponse> istanbulPropose(String address, boolean auth);

    Request<?, ConsensusNoResponse> istanbulDiscard(String address);

    Request<?, IstanbulCandidates> istanbulCandidates();

    Request<?, IstanbulNodeAddress> istanbulNodeAddress();

    Request<?, IstanbulBlockSigners> istanbulGetSignersFromBlock(String blockNum);

    Request<?, IstanbulBlockSigners> istanbulGetSignersFromBlockByHash(String blockHash);

    // permissioning

    Request<?, PermissionOrgList> quorumPermissionGetOrgList();

    Request<?, PermissionNodeList> quorumPermissionGetNodeList();

    Request<?, PermissionRoleList> quorumPermissionGetRoleList();

    Request<?, PermissionAccountList> quorumPermissionGetAccountList();

    Request<?, ExecStatusInfo> quorumPermissionAddOrg(
            String orgId, String enodeId, String address, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionApproveOrg(
            String orgId, String enodeId, String address, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionAddSubOrg(
            String pOrgId, String orgId, String enodeId, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionUpdateOrgStatus(
            String orgId, int action, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionApproveOrgStatus(
            String orgId, int action, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionAddNode(
            String orgId, String enodeId, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionUpdateNodeStatus(
            String orgId, String enodeId, int action, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionAssignAdminRole(
            String orgId, String address, String roleid, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionApproveAdminRole(
            String orgId, String address, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionAddNewRole(
            String orgId,
            String roleId,
            int access,
            boolean isVoter,
            boolean isAdmin,
            PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionRemoveRole(
            String orgId, String roleId, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionAddAccountToOrg(
            String address, String orgId, String roleId, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionChangeAccountRole(
            String address, String orgId, String roleId, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionUpdateAccountStatus(
            String orgId, String address, int status, PrivateTransaction transaction);

    Request<?, OrgDetailsInfo> quorumPermissionGetOrgDetails(String orgId);

    Request<?, ExecStatusInfo> quorumPermissionRecoverBlackListedNode(
            String orgId, String enodeId, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionApproveBlackListedNodeRecovery(
            String orgId, String enodeId, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionRecoverBlackListedAccount(
            String orgId, String address, PrivateTransaction transaction);

    Request<?, ExecStatusInfo> quorumPermissionApproveBlackListedAccountRecovery(
            String orgId, String address, PrivateTransaction transaction);

    Request<?, ActiveExtensionList> quorumExtensionActiveExtensionContracts();

    Request<?, ApproveExtensionInfo> quorumExtensionApproveExtension(
            String addressToVoteOn, boolean vote, PrivateTransaction transaction);

    Request<?, CancelExtensionInfo> quorumExtensionCancelExtension(
            String extensionContract, PrivateTransaction transaction);

    Request<?, ExtendContractInfo> quorumExtensionExtendContract(
            String toExtend,
            String newRecipientPtmPublicKey,
            String recipientAddress,
            PrivateTransaction transaction);

    Request<?, ExtensionStatusInfo> quorumExtensionGetExtensionStatus(
            String managementContractAddress);

    Request<?, ExtensionApprovalUuid> quorumExtensionGenerateExtensionApprovalUuid(
            String addressToVoteOn, String externalSignerAddress, PrivateTransaction transaction);
}
