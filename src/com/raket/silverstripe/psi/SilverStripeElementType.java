package com.raket.silverstripe.psi;

import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.SilverStripeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-16
 * Time: 22:36
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeElementType extends IElementType {
    public SilverStripeElementType(@NotNull @NonNls String debugName) {
        super(debugName, SilverStripeLanguage.INSTANCE);
    }
}
