package com.me.steel.Domain;

import com.me.steel.Domain.Entities.TweenAnimations.FightableEntity;

public class Customer {
	private FightableEntity entity;
	private boolean ordered;
	private boolean ready;
	private boolean delivered;
	
	public FightableEntity getEntity() {
		return entity;
	}
	
	public boolean hasOrdered() {
		return ordered;
	}
	
	public boolean gotDelivery() {
		return delivered;
	}
	
	public boolean isReadyToOrder() {
		return ready;
	}
	
	public void setEntity(FightableEntity entity) {
		this.entity = entity;
	}
	
	public void setReadyToOder(boolean ready) {
		this.ready = ready;
	}
	
	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}
	
	public void setDelivered(boolean delivered) {
		this.delivered = delivered;
	}
}
