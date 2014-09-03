/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.util;

import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

/**
 *
 * @author Sandy Duan
 */
public class VelocityUtils {
    
    /**
	 * Render text by velocity template engine
	 * @param str
	 *            
	 * @param objMap
	 *            
	 * @return
	 */
	public static  String StringEvaluate(String str, Map<String, Object> objMap) {
		Context context = new VelocityContext(objMap);
		Writer writer = new StringWriter();
		Velocity.evaluate(context, writer, "", str);
		return writer.toString();
	}
}
