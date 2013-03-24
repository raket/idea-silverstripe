package com.raket.silverstripe.psi;

import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.SilverStripeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-21
 * Time: 22:51
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeCompositeElementType extends IElementType {
    public SilverStripeCompositeElementType(@NotNull @NonNls String debugName) {
        super(debugName, SilverStripeLanguage.INSTANCE);
    }
}
