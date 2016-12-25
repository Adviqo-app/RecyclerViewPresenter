package net.kibotu.android.recyclerviewpresenter

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

open class BaseViewHolder
/**
 * Creates a new ViewHolder and calls [.onBindViewHolder] in order to bind the view using [ButterKnife].

 * @param itemView
 */
(itemView: View) : RecyclerView.ViewHolder(itemView), IBaseViewHolder {

    protected var unbinder: Unbinder? = null

    init {
        onBindViewHolder()
    }

    /**
     * Inflates the layout and binds it to the ViewHolder using [ButterKnife].
     */
    constructor(@LayoutRes layout: Int, parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(layout, parent, false)) {
    }

    /**
     * Binds all views to the ViewHolder using ButterKnife.
     */
    override fun onBindViewHolder() {
        if (unbinder == null)
            unbinder = ButterKnife.bind(this, itemView)
    }

    /**
     * Is called when it gets detached from the view.
     * It also removes all listeners like click, focusChange listeners and background selectors.
     */
    @CallSuper
    override fun onViewDetachedFromWindow() {
        if (unbinder != null) {
            unbinder!!.unbind()
            unbinder = null
        }
    }
}