package org.dareon.json;

import java.util.List;
import org.dareon.domain.Classification;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonClassificationTree {

	private static JSONObject addChildren(JSONObject classification, List<Classification> classificationChildren) {
		// System.out.println(fOR);
		JSONArray classificationChildrenArray = new JSONArray();
		for (Classification a : classificationChildren) {
			JSONObject classificationChildObject = new JSONObject();
			classificationChildObject.put("text", a.getCode() + " | " + a.getName());
			classificationChildObject.put("id", a.getId().toString());
			classificationChildObject.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
			if (a.getChildren().size() > 0)
				addChildren(classificationChildObject, a.getChildren());
			// System.out.println(fORChildObject);
			classificationChildrenArray.put(classificationChildObject);
		}

		classification.put("nodes", classificationChildrenArray);
		System.out.println("final: " + classification);
		return classification;
	}

	public static String getClassificationTreeAsString(List<Classification> classifications) {
		// System.out.println(fOR);
		JSONObject obj = new JSONObject();
		JSONArray arr = new JSONArray();
		// return aNZSRCService.list();
		for (Classification a : classifications) {
			obj.put("text", a.getCode() + " | " + a.getName());
			obj.put("id", a.getId());
			obj.put("tags", new JSONArray().put(String.valueOf(a.getChildren().size())));
			if (a.getChildren().size() > 0)
				obj = addChildren(obj, a.getChildren());
			arr.put(obj);
			obj = new JSONObject();
		}
		return arr.toString();
	}

}