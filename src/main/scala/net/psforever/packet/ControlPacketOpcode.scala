// Copyright (c) 2016 PSForever.net to present
package net.psforever.packet

import net.psforever.packet.control.SlottedMetaPacket
import scodec.bits.BitVector
import scodec.{Attempt, Codec, DecodeResult, Err}
import scodec.codecs._

object ControlPacketOpcode extends Enumeration {
  type Type = Value
  val

  // Opcodes should have a marker every 10
  // OPCODE 0
  HandleGamePacket, // a whoopsi case: not actually a control packet, but a game packet
  ClientStart, // first packet ever sent during client connection
  ServerStart, // second packet sent in response to ClientStart
  MultiPacket, // used to send multiple packets with one UDP message (subpackets limited to <= 255)
  Unknown4,
  Unknown5,
  Unknown6,
  ControlSync, // sent to the server from the client
  ControlSyncResp, // the response generated by the server
  SlottedMetaPacket0,

  // OPCODE 10
  SlottedMetaPacket1,
  SlottedMetaPacket2,
  SlottedMetaPacket3,
  SlottedMetaPacket4,
  SlottedMetaPacket5,
  SlottedMetaPacket6,
  SlottedMetaPacket7,
  RelatedA0,
  RelatedA1,
  RelatedA2,

  // OPCODE 20
  RelatedA3,
  RelatedB0,
  RelatedB1,
  RelatedB2,
  RelatedB3,
  AggregatePacket, // same as MultiPacket, but with the ability to send extended length packets
  Unknown26,
  Unknown27,
  Unknown28,
  ConnectionClose,

  // OPCODE 30
  Unknown30
  = Value

  private def noDecoder(opcode : ControlPacketOpcode.Type) = (a : BitVector) =>
    Attempt.failure(Err(s"Could not find a marshaller for control packet ${opcode}"))

  def getPacketDecoder(opcode : ControlPacketOpcode.Type) : (BitVector) => Attempt[DecodeResult[PlanetSideControlPacket]] = opcode match {
      // OPCODE 0
      case HandleGamePacket => control.HandleGamePacket.decode
      case ServerStart => control.ServerStart.decode
      case ClientStart => control.ClientStart.decode
      case MultiPacket => control.MultiPacket.decode
      case Unknown4 => noDecoder(opcode)
      case Unknown5 => noDecoder(opcode)
      case Unknown6 => noDecoder(opcode)
      case ControlSync => control.ControlSync.decode
      case ControlSyncResp => control.ControlSyncResp.decode
      case SlottedMetaPacket0 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket0)

      // OPCODE 10
      case SlottedMetaPacket1 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket1)
      case SlottedMetaPacket2 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket2)
      case SlottedMetaPacket3 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket3)
      case SlottedMetaPacket4 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket4)
      case SlottedMetaPacket5 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket5)
      case SlottedMetaPacket6 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket6)
      case SlottedMetaPacket7 => SlottedMetaPacket.decodeWithOpcode(SlottedMetaPacket7)
      case RelatedA0 => noDecoder(opcode)
      case RelatedA1 => noDecoder(opcode)
      case RelatedA2 => noDecoder(opcode)

      // OPCODE 20
      case RelatedA3 => noDecoder(opcode)
      case RelatedB0 => noDecoder(opcode)
      case RelatedB1 => noDecoder(opcode)
      case RelatedB2 => noDecoder(opcode)
      case RelatedB3 => noDecoder(opcode)
      case AggregatePacket => noDecoder(opcode)
      case Unknown26 => noDecoder(opcode)
      case Unknown27 => noDecoder(opcode)
      case Unknown28 => noDecoder(opcode)
      case ConnectionClose => control.ConnectionClose.decode

      // OPCODE 30
      case Unknown30 => noDecoder(opcode)
      case default => noDecoder(opcode)
  }

  implicit val codec: Codec[this.Value] = PacketHelpers.createEnumerationCodec(this, uint8L)
}
