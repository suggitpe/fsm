package org.suggs.fsm.common;

import org.apache.commons.lang3.builder.ToStringStyle;

public class StringStyle {

    public static final String EMPTY_ARRAY = "{}";

    /**
     * ToStringBuilder style which is exactly the same as the
     * ToStringStyle default except it uses short class names instead
     * of fully qualified ones.
     */
    public static final ToStringStyle DEFAULT_TO_STRING_STYLE = new DefaultStringStyle();

    /**
     * ToStringBuilder style which is exactly the same as the
     * ToStringStyle default except it uses short class names instead
     * of fully qualified ones.
     */
    private static final class DefaultStringStyle extends ToStringStyle {

        /**
         * Initialises the settings of the DefaultStringStyle (i.e.
         * setUseShortClassName(true)).
         */
        private DefaultStringStyle() {
            setUseShortClassName(true);
        }

        /**
         * Required for serialisation and equality requirements
         * (returns the static final instance of this class).
         *
         * @return The static final instance of this class.
         */
        protected Object readResolve() {
            return StringStyle.DEFAULT_TO_STRING_STYLE;
        }

    }
}
