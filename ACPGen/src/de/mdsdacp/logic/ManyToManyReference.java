package de.mdsdacp.logic;

import org.eclipse.emf.ecore.EReference;

public class ManyToManyReference {

	private String name = "";
	private String[] classes = new String[2];
	
	public ManyToManyReference(EReference ref) {
		String className = ref.getEType().getName();
		String opClassName = ref.getEOpposite().getName();
		name = className + "_" + opClassName;
		classes[0] = className;
		classes[1] = opClassName;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}
	
	
}
