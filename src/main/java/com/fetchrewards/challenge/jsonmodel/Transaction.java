package com.fetchrewards.challenge.jsonmodel;



import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transaction {
	
	public String payer;
	public int points;
	public String timeStamp;
}
