package com.fetchrewards.challenge.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fetchrewards.challenge.jsonmodel.SpendReq;
import com.fetchrewards.challenge.jsonmodel.Transaction;
import com.fetchrewards.challenge.util.Constants;
import com.fetchrewards.challenge.vo.PayerPoints;
import com.fetchrewards.challenge.vo.PointsInfo;

@Service("PaymentRewardsService")
public class PaymentRewardsService {
	int total_points = 0;
	Map<String, PointsInfo> payerToPointsMap = new HashMap<String, PointsInfo>();
	PriorityQueue<Transaction> transactions_queue = new PriorityQueue<Transaction>((a, b) -> a.timeStamp.compareTo(b.timeStamp));
/**
 * 
 * @param transaction
 * @return
 * @throws Exception
 */
	public String addTransaction(Transaction transaction) throws Exception {
        
		// No Transaction added if points are zero.
		if (transaction.points == 0) {
			throw new Exception(Constants.ZERO_POINTS);
		} else if (transaction.points < 0) {
			// Check if there is enough balance.
			if (this.total_points + transaction.points < 0) {
				throw new Exception(Constants.INSUFFICIENT_BALANCE);
			}
			// check if payer has sufficient balance
			if (!payerToPointsMap.containsKey(transaction.payer)
					|| payerToPointsMap.get(transaction.payer).getTotal_payerpoints() + transaction.points < 0) {
				throw new Exception(Constants.INSUFFICIENT_PAYERS_BALANCE);
			}
			//store the points to be deleted and update payer total points and user points
			updateState(transaction, true);
		} else {
			//Add to Queue and update payer total points and user points.
			updateState(transaction, false);
		}
		return Constants.TRANSACTION_SUCCESS;
	}

/**
 * 	
 * @param req
 * @return
 * @throws Exception
 */
	public String spendPoints(SpendReq req) throws Exception {
		int points = req.points;
		HashMap<String, Integer> result = new HashMap<>();
		// Check if there is enough balance.
		if (this.total_points < points) {
			throw new Exception(Constants.INSUFFICIENT_BALANCE);
		}
		while (points > 0 && transactions_queue.size() > 0) {
			Transaction curr = transactions_queue.peek();
			PointsInfo pt = payerToPointsMap.get(curr.payer);
			
			// Pending points stores points spent.
			int payerPendingPoints = pt.getPending_points();
			
			// check if payer has any points that should be deleted from old transactions.
			if (payerPendingPoints < 0) {
				int cutPoints = payerPendingPoints + curr.points;
				if (cutPoints <= 0) {
					// Poll the transaction object from queue and update pending points.
					payerPendingPoints = cutPoints;
					transactions_queue.poll();
					continue;
				} else {
					payerPendingPoints = 0;
					curr.points = cutPoints;
				}
				pt.setPending_points(payerPendingPoints);
			}
			
			/* There are 2 cases here 
			    1. Points are greater than the oldest transaction points.
			    2. Points are less than the oldest transaction points.   
			 */
			
			if (points >= curr.points) {
				updatePoints(pt, curr.points, result, curr);
				points -= curr.points;
				transactions_queue.poll();
			} else if (points < curr.points) {
				curr.points -= points;
				updatePoints(pt, points, result, curr);
				points = 0;
			}
		}
		List<PayerPoints> pointsDeducted = new ArrayList<>();
		// Add the points spent per payer to the pointsDeducted list.
		for (String payer : result.keySet()) {
			PayerPoints pp = new PayerPoints();
			pp.setPayer(payer);
			pp.setPoints(result.get(payer));
			pointsDeducted.add(pp);
		}
		ObjectMapper Obj = new ObjectMapper();
		return Obj.writeValueAsString(pointsDeducted);
	}
	
/**
 * 
 * @return
 * @throws Exception
 */
	public String getPayerPoints() throws Exception {
		List<PayerPoints> payerPointsList = new ArrayList<>();
		// Add the details of points per payer to the payerPointsList.
		for (Map.Entry<String, PointsInfo> entry : payerToPointsMap.entrySet()) {
			PayerPoints pp = new PayerPoints();
			pp.setPayer(entry.getKey());
			pp.setPoints(entry.getValue().getTotal_payerpoints());
			payerPointsList.add(pp);
		}
		ObjectMapper Obj = new ObjectMapper();
		return Obj.writeValueAsString(payerPointsList);
	}

	void updateState(Transaction t, boolean isSpendTransaction) {
		if (!payerToPointsMap.containsKey(t.payer))
			payerToPointsMap.put(t.payer, new PointsInfo());
		PointsInfo temp = payerToPointsMap.get(t.payer);
		temp.setTotal_payerpoints(temp.getTotal_payerpoints() + t.points);
		if (isSpendTransaction)
			temp.setPending_points(temp.getPending_points() + t.points);
		else
			transactions_queue.offer(t);
		this.total_points += t.points;
	}

	void updatePoints(PointsInfo pt, int subPoints, HashMap<String, Integer> result, Transaction curr) {
		pt.setTotal_payerpoints(pt.getTotal_payerpoints() - subPoints);
		this.total_points -= subPoints;
		result.put(curr.payer, result.getOrDefault(curr.payer, 0) - subPoints);
	}
}
