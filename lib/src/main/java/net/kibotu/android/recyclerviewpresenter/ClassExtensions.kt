package net.kibotu.android.recyclerviewpresenter

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

internal object ClassExtensions {

    /**
     * Compares two classes by canonical name.

     * @return `True` if they're equal.
     */
    fun equals(first: Class<*>, second: Class<*>): Boolean {
        return first.canonicalName == second.canonicalName
    }
}
