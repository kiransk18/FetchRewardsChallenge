package com.fetchrewards.challenge.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fetchrewards.challenge.jsonmodel.SpendReq;
import com.fetchrewards.challenge.jsonmodel.Transaction;
import com.fetchrewards.challenge.service.PaymentRewardsService;

import io.swagger.annotations.ApiOperation;


@RestController
@RequestMapping("api/v1/fetchrewards")
public class PaymentRewardsRestController {
	
	
	@Autowired
	PaymentRewardsService paymentRewardsService;
	
	@ApiOperation(value = "This API adds a rewards transaction to the system")
	@PostMapping(value= "/addTransaction")
	public ResponseEntity<String> addTransaction(@RequestBody Transaction transactionRequest)
	{
		String response = null;
		
		try {
			response = paymentRewardsService.addTransaction(transactionRequest);
			return new ResponseEntity<>(
					response, 
				      HttpStatus.OK);
		}
		catch(Exception ex){
			return new ResponseEntity<>(
					ex.getMessage(), 
				      HttpStatus.INTERNAL_SERVER_ERROR);
			
		}	
	}
	
	@ApiOperation(value = "This API returns points spent per payer")
	@PostMapping(value= "/spendPoints")
	public ResponseEntity<String> spendPoints(@RequestBody SpendReq spendReq)
	{
		String spendResponse = null;
		
		try {
			spendResponse = paymentRewardsService.spendPoints(spendReq);
			return new ResponseEntity<>(
					spendResponse, 
				      HttpStatus.OK);
		}
		catch(Exception ex){
			return new ResponseEntity<>(
					ex.getMessage(), 
				      HttpStatus.INTERNAL_SERVER_ERROR);	
		}	
	}
	
	
	@ApiOperation(value = "This API returns all the payers along with points")
	@GetMapping(value= "/getPayerPoints")
	public ResponseEntity<String> getPayerPoints()
	{
		String payerpointsResponse = null;
		
		try {
			payerpointsResponse = paymentRewardsService.getPayerPoints();
			return new ResponseEntity<>(
					payerpointsResponse, 
				      HttpStatus.OK);
		}
		catch(Exception ex){
			return new ResponseEntity<>(
					ex.getMessage(), 
				      HttpStatus.INTERNAL_SERVER_ERROR);
			
		}	
	}
}
