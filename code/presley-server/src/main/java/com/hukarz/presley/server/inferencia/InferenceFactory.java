/**
 * 
 */
package com.hukarz.presley.server.inferencia;

import com.hukarz.presley.server.core.PresleyProperties;
import com.hukarz.presley.server.core.StartPresleyServer;

/**
 * @author Alan Kelon Oliveira de Moraes <alan@di.ufpb.br>
 *
 */
public class InferenceFactory {
	
	enum InferenceAlgorithm {
		trindadeMSc, line10
	}
	
	public static Inference createInstance() {
		PresleyProperties properties = PresleyProperties.getInstance();
		InferenceAlgorithm algorithm = 	InferenceAlgorithm.valueOf(properties.getProperty("inference.algorithm"));
		boolean useCommunicationHistory = Boolean.valueOf(properties.getProperty("inference.useCommunicationHistory"));
		boolean useCodeHistory = Boolean.valueOf(properties.getProperty("inference.useCodeHistory"));
		
		switch (algorithm) {
		case trindadeMSc:
			return new Inferencia(useCommunicationHistory, useCodeHistory);
		case line10:
			return new InferenciaLine10();
		default:
			return new Inferencia(useCommunicationHistory, useCodeHistory);
		}
	}
}
