package inputTest;

import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;


public class Visitor extends ASTVisitor{


	ArrayList<String> impDec = new ArrayList<String>();
	HashMap<String, String> methodInv = new HashMap<String, String>();



	@Override
	public boolean visit(ImportDeclaration node) {
		if(node.getName().toString().contains("android.accessibilityservice")
				|| node.getName().toString().contains("android.view.accessibility")) {
			impDec.add(node.getName().toString());
		}
		return true;

	}

	@Override
	public boolean visit(MethodInvocation node) {

		String method = node.getName().toString();
		String variable;
		if(method.contains("setContentDescription") ||
				method.contains("setHint")||method.contains("findView")){
			if(node.getExpression()==null) 
				variable = "null";

			else
				variable = node.getExpression().toString();
			methodInv.put(variable+ "." + method, node.arguments().toString());

		}

		return true;
	}

	public ArrayList<String> getImport(){
		return impDec;
	}


	public HashMap<String, String> getMethodInvoke(){
		return methodInv;
	}

}
