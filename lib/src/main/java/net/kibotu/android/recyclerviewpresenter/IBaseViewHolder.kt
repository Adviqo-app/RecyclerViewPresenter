package net.kibotu.android.recyclerviewpresenter

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

interface IBaseViewHolder {

    fun onBindViewHolder()

    fun onViewDetachedFromWindow()
}
