package com.canevi.maths;

import java.util.Arrays;

public class Matrixf {
	private float[] elements;
    private int m, n;

    public Matrixf(int m, int n) {
        this.m = m;
        this.n = n;
        this.elements = new float[m * n];
    }

    public Matrixf(int m, int n, float... elements) {
        this.m = m;
        this.n = n;
        this.elements = elements;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Matrixf other = (Matrixf) obj;
		if (!Arrays.equals(elements, other.elements))
			return false;
		return true;
	}

	public float get(int x, int y) {
		return elements[y * n + x];
	}
	
	public void set(int x, int y, float value) {
		elements[y * n + x] = value;
	}
	
	public float[] getAll() {
		return elements;
	}

	public void setAll(float[] elements) {
		if(elements.length != m * n) {
			throw new IllegalArgumentException("Matrix size must be " + m + "x" + n);
		}
		this.elements = elements;
	}

}
