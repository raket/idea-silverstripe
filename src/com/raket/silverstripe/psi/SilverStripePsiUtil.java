package com.raket.silverstripe.psi;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementResolveResult;
import com.intellij.psi.ResolveResult;
import com.intellij.psi.tree.TokenSet;
import com.intellij.psi.util.PsiElementFilter;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.php.PhpIndex;
import com.jetbrains.php.lang.psi.elements.Method;
import com.jetbrains.php.lang.psi.elements.PhpClass;
import com.jetbrains.php.lang.psi.elements.impl.ArrayHashElementImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.raket.silverstripe.psi.SilverStripeTypes.*;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-28
 * Time: 23:26
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripePsiUtil {

	private static TokenSet START_TOKENS = TokenSet.create(
			SS_BLOCK_START_STATEMENT,
			SS_IF_STATEMENT,
			SS_CACHED_STATEMENT
	);

	private static TokenSet END_TOKENS = TokenSet.create(
			SS_BLOCK_END_STATEMENT
	);

	private static Collection<PhpClass> PHP_CLASSES = null;
	private static PhpClass[] CLASS_LIST = null;

	/**
	 * Used to determine if an element is part of an "open tag" (i.e. "{{#open}}" or "{{^openInverse}}")
	 * If the given element is the descendant of an {@link com.dmarcotte.handlebars.parsing.HbTokenTypes#OPEN_BLOCK_STACHE}
	 * or an {@link com.dmarcotte.handlebars.parsing.HbTokenTypes#OPEN_INVERSE_BLOCK_STACHE}, this method returns
	 * that parent.
	 *
	 * Otherwise, returns null.
	 *
	 * @param element The element whose ancestors will be searched
	 * @return An ancestor of type {@link com.dmarcotte.handlebars.parsing.HbTokenTypes#OPEN_BLOCK_STACHE}
	 * or {@link com.dmarcotte.handlebars.parsing.HbTokenTypes#OPEN_INVERSE_BLOCK_STACHE} or null if none exists
	 */
	public static PsiElement findParentOpenTagElement(PsiElement element) {
		return PsiTreeUtil.findFirstParent(element, true, new Condition<PsiElement>() {
			@Override
			public boolean value(PsiElement element) {
				return element != null
						&& element.getNode() != null
						&& START_TOKENS.contains( element.getNode().getElementType());
			}
		});
	}

	/**
	 * Used to determine if an element is part of a "close tag" (i.e. "{{/closer}}")
	 *
	 * If the given element is the descendant of an {@link com.dmarcotte.handlebars.parsing.HbTokenTypes#CLOSE_BLOCK_STACHE},
	 * this method returns that parent.
	 *
	 * Otherwise, returns null.
	 *
	 * @param element The element whose ancestors will be searched
	 * @return An ancestor of type {@link com.dmarcotte.handlebars.parsing.HbTokenTypes#CLOSE_BLOCK_STACHE}
	 * or null if none exists
	 */
	public static PsiElement findParentCloseTagElement(PsiElement element) {
		return PsiTreeUtil.findFirstParent(element, true, new Condition<PsiElement>() {
			@Override
			public boolean value(PsiElement element) {
				return element != null
						&& element.getNode() != null
						&& END_TOKENS.contains(element.getNode().getElementType());
			}
		});
	}

	/**
	 * Tests to see if the given element is not the "root" statements expression of the grammar
	 */
	public static boolean isNonRootStatementsElement(PsiElement element) {
		PsiElement statementsParent = PsiTreeUtil.findFirstParent(element, true, new Condition<PsiElement>() {
			@Override
			public boolean value(PsiElement element) {
				return element != null
						&& element.getNode() != null
						&& element.getNode().getElementType() == SS_STATEMENTS;
			}
		});

		// we're a non-root statements if we're of type statements, and we have a statements parent
		return element.getNode().getElementType() == SS_STATEMENTS
				&& statementsParent != null;
	}

	private static boolean checkClass(PhpClass phpClass) {
		String[] implementsList = phpClass.getInterfaceNames();
		PhpClass parentClass = phpClass.getSuperClass();
		boolean addToList = true;
		for (String impInterface : implementsList) {
			if (impInterface.contains("TestOnly")) {
				addToList = false;
				break;
			}
		}

		if (parentClass != null && parentClass.getName().contains("BuildTask"))  {
			addToList = false;
		}

		return addToList;
	}

	public static List<ResolveResult> getFieldMethodResolverResults(Project project, String key) {
		List<ResolveResult> results = new ArrayList<ResolveResult>();
		List<String> checkedClasses = new ArrayList<String>();
		List<String> checkedArrayClasses = new ArrayList<String>();
		//CopyOnWriteArrayList<PhpClass> iteratorList;

		PhpIndex phpIndex = PhpIndex.getInstance(project);
/*		Collection<PhpClass> classes = phpIndex.getAllSubclasses("Object");
		Collection<PhpClass> extensionClasses = phpIndex.getAllSubclasses("Extension");
		classes.addAll(extensionClasses);

		*/
		if (PHP_CLASSES == null) {
			PHP_CLASSES = phpIndex.getAllSubclasses("Object");
			PHP_CLASSES.addAll(phpIndex.getAllSubclasses("Extension"));
			if (CLASS_LIST == null)
				CLASS_LIST = PHP_CLASSES.toArray(new PhpClass[PHP_CLASSES.size()]);
		}
		//iteratorList = new CopyOnWriteArrayList<PhpClass>(classes);
		for (PhpClass phpClass : CLASS_LIST) {
			if (!checkClass(phpClass))
				continue;

			String workingKey = key;
			if (phpClass != null) {
				Method phpMethod = phpClass.findOwnMethodByName(workingKey);
				if (phpMethod == null) {
					workingKey = "get"+key;
					phpMethod = phpClass.findOwnMethodByName(workingKey);
				}
				if (phpMethod != null && !checkedClasses.contains(phpClass.getName())) {
					checkedClasses.add(phpClass.getName());
					results.add(new PsiElementResolveResult(phpMethod));
				}

				PsiElement[] arraySearches = {
					phpClass.findOwnFieldByName("db", false),
					phpClass.findOwnFieldByName("has_one", false),
					phpClass.findOwnFieldByName("has_many", false),
					phpClass.findOwnFieldByName("many_many", false),
					phpClass.findOwnFieldByName("belongs_many_many", false)
				};
				for (PsiElement arraySearch : arraySearches) {
					if (arraySearch != null) {
						PsiElement[] arrayKeys = PsiTreeUtil.collectElements(arraySearch, new PsiElementFilter() {
							@Override
							public boolean isAccepted(PsiElement element) {
								return element instanceof ArrayHashElementImpl;
							}
						});
						for (PsiElement arrayHash : arrayKeys) {
							String childText = arrayHash.getFirstChild().getText();
							childText = childText.substring(1, childText.length()-1);
							if (childText.equals(key) && !checkedArrayClasses.contains(phpClass.getName())) {
								checkedArrayClasses.add(phpClass.getName());
								results.add(new PsiElementResolveResult(arrayHash.getFirstChild()));
							}
						}
					}
				}
			}
		}
		return results;
	}
}