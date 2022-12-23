/*
 * This file is part of Bisq.
 *
 * Bisq is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at
 * your option) any later version.
 *
 * Bisq is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public
 * License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with Bisq. If not, see <http://www.gnu.org/licenses/>.
 */

package bisq.chat.channel.private_two_party;

import bisq.chat.channel.ChannelDomain;
import bisq.chat.channel.ChannelNotificationType;
import bisq.chat.channel.PrivateChannel;
import bisq.common.data.Pair;
import bisq.user.identity.UserIdentity;
import bisq.user.profile.UserProfile;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@ToString(callSuper = true)
@Getter
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public final class PrivateTwoPartyChannel extends PrivateChannel<PrivateTwoPartyChatMessage> {
    private final UserProfile peer;

    public PrivateTwoPartyChannel(UserProfile peer, UserIdentity myUserIdentity, ChannelDomain channelDomain) {
        this(channelDomain,
                PrivateChannel.createChannelName(new Pair<>(peer.getId(), myUserIdentity.getId())),
                peer,
                myUserIdentity,
                new ArrayList<>(),
                ChannelNotificationType.ALL
        );
    }

    private PrivateTwoPartyChannel(ChannelDomain channelDomain,
                                   String channelName,
                                   UserProfile peer,
                                   UserIdentity myProfile,
                                   List<PrivateTwoPartyChatMessage> chatMessages,
                                   ChannelNotificationType channelNotificationType) {
        super(channelDomain, channelName, myProfile, chatMessages, channelNotificationType);

        this.peer = peer;
    }

    @Override
    public bisq.chat.protobuf.Channel toProto() {
        return getChannelBuilder().setPrivateTwoPartyChannel(bisq.chat.protobuf.PrivateTwoPartyChannel.newBuilder()
                        .setPeer(peer.toProto())
                        .setMyUserIdentity(myUserIdentity.toProto())
                        .addAllChatMessages(chatMessages.stream()
                                .map(PrivateTwoPartyChatMessage::toChatMessageProto)
                                .collect(Collectors.toList())))
                .build();
    }

    public static PrivateTwoPartyChannel fromProto(bisq.chat.protobuf.Channel baseProto,
                                                   bisq.chat.protobuf.PrivateTwoPartyChannel proto) {
        return new PrivateTwoPartyChannel(
                ChannelDomain.fromProto(baseProto.getChannelDomain()),
                baseProto.getId(),
                UserProfile.fromProto(proto.getPeer()),
                UserIdentity.fromProto(proto.getMyUserIdentity()),
                proto.getChatMessagesList().stream()
                        .map(PrivateTwoPartyChatMessage::fromProto)
                        .collect(Collectors.toList()),
                ChannelNotificationType.fromProto(baseProto.getChannelNotificationType())
        );
    }

    @Override
    public void addChatMessage(PrivateTwoPartyChatMessage chatMessage) {
        chatMessages.add(chatMessage);
    }

    @Override
    public void removeChatMessage(PrivateTwoPartyChatMessage chatMessage) {
        chatMessages.remove(chatMessage);
    }

    @Override
    public void removeChatMessages(Collection<PrivateTwoPartyChatMessage> removeMessages) {
        chatMessages.removeAll(removeMessages);
    }

    @Override
    public String getDisplayString() {
        return peer.getUserName() + "-" + myUserIdentity.getUserName();
    }
}