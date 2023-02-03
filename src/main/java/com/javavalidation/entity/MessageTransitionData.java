package com.javavalidation.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.ToString;

@Entity
@Data
@Table(name = "message_transition")
@ToString
public class MessageTransitionData {
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "receiver_bic")
	private String receiverBic;
	
	@Column(name = "sender_bic")
	private String senderBic;
	
	@Column(name = "msgtype")
	private String msgType;
	
	@Column(name = "account")
	private String account;
	
	@Column(name = "currency")
	private String ccy;
	
	@Column(name = "statement_number")
	private String statementNumber;
	
	@Column(name = "sequence_number")
	private String sequenceNumber;
	
	@Column(name = "is_final_seq_num")
	private String isFinalSequenceNumber;
	
	@Column(name = "length_msg")
	private String lengthMsg;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "received_date")
	private Date receivedDate;
	
	@Column(name = "closing_balance")
	private String closingBalance;
	
	@Id
	@Column(name = "message_id")
	private Integer messageId;

}
