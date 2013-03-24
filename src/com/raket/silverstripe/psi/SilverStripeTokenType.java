package com.raket.silverstripe.psi;

import com.intellij.psi.tree.IElementType;
import com.raket.silverstripe.SilverStripeLanguage;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Created with IntelliJ IDEA.
 * User: Marcus Dalgren
 * Date: 2013-03-16
 * Time: 22:32
 * To change this template use File | Settings | File Templates.
 */
public class SilverStripeTokenType extends IElementType {

    public SilverStripeTokenType(@NotNull @NonNls String debugName) {
        super(debugName, SilverStripeLanguage.INSTANCE);
    }

    @Override
    public String toString() {
        return "SilverStripeTokenType." + super.toString();
    }
}
