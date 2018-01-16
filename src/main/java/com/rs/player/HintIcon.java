package com.rs.player;

public final class HintIcon {

	private int coordX;
	private int coordY;
	private int plane;
	private int distanceFromFloor;
	private int targetType;
	private int targetIndex;
	private int arrowType;
	private int modelId;
	private int index;

	public HintIcon() {
		this.setIndex(7);
	}

	public HintIcon(final int targetType, final int modelId, final int index) {
		this.setTargetType(targetType);
		this.setModelId(modelId);
		this.setIndex(index);
	}

	public HintIcon(final int targetIndex, final int targetType,
			final int arrowType, final int modelId, final int index) {
		this.setTargetType(targetType);
		this.setTargetIndex(targetIndex);
		this.setArrowType(arrowType);
		this.setModelId(modelId);
		this.setIndex(index);
	}

	public HintIcon(final int coordX, final int coordY, final int height,
			final int distanceFromFloor, final int targetType,
			final int arrowType, final int modelId, final int index) {
		this.setCoordX(coordX);
		this.setCoordY(coordY);
		this.setPlane(height);
		this.setDistanceFromFloor(distanceFromFloor);
		this.setTargetType(targetType);
		this.setArrowType(arrowType);
		this.setModelId(modelId);
		this.setIndex(index);
	}

	public void setTargetType(final int targetType) {
		this.targetType = targetType;
	}

	public int getTargetType() {
		return targetType;
	}

	public void setTargetIndex(final int targetIndex) {
		this.targetIndex = targetIndex;
	}

	public int getTargetIndex() {
		return targetIndex;
	}

	public void setArrowType(final int arrowType) {
		this.arrowType = arrowType;
	}

	public int getArrowType() {
		return arrowType;
	}

	public void setModelId(final int modelId) {
		this.modelId = modelId;
	}

	public int getModelId() {
		return modelId;
	}

	public void setIndex(final int modelPart) {
		this.index = modelPart;
	}

	public int getIndex() {
		return index;
	}

	public void setCoordX(final int coordX) {
		this.coordX = coordX;
	}

	public int getCoordX() {
		return coordX;
	}

	public void setCoordY(final int coordY) {
		this.coordY = coordY;
	}

	public int getCoordY() {
		return coordY;
	}

	public void setPlane(final int plane) {
		this.plane = plane;
	}

	public int getPlane() {
		return plane;
	}

	public void setDistanceFromFloor(final int distanceFromFloor) {
		this.distanceFromFloor = distanceFromFloor;
	}

	public int getDistanceFromFloor() {
		return distanceFromFloor;
	}
}
