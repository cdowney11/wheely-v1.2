package com.alkliv.wheely;

import java.util.HashMap;
import java.util.List;

public interface IFaceTaskParsePlaces {
	public void startTaskParsePlaces(String result);
	public void onEndTaskParsePlaces(List<HashMap<String,String>> list,Place[] placeArray);
}
