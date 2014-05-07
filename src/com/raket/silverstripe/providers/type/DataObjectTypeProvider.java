package com.raket.silverstripe.providers.type;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.MethodReference;
import com.jetbrains.php.lang.psi.elements.PhpExpression;
import com.jetbrains.php.lang.psi.elements.PhpNamedElement;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeProvider2;
import com.jetbrains.php.lang.psi.resolve.types.PhpTypeSignatureKey;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class DataObjectTypeProvider implements PhpTypeProvider2 {
	@Override
	public char getKey() {
		return 'S';
	}

	@Nullable
	@Override
	public String getType(PsiElement psiElement) {
		if((psiElement instanceof MethodReference) && ((MethodReference) psiElement).isStatic()) {
			String className;
			MethodReference psiMethod = (MethodReference) psiElement;
			String methodName = psiMethod.getName();
			if (methodName.equals("create")) {
				PhpExpression classReference = psiMethod.getClassReference();
				if (classReference != null) {
					className = classReference.getName();
					return className+"."+methodName;
				}
			}
		}
		return null;
	}

	@Override
	public Collection<? extends PhpNamedElement> getBySignature(String expression, Project project) {
		boolean gotHere = true;
		return Collections.emptySet();
	}
}
