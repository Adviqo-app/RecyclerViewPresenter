package net.kibotu.android.recyclerviewpresenter

import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

abstract class Presenter<T, VH : RecyclerView.ViewHolder>(
        /**
         * Respective adapter.
         */
        protected val presenterAdapter: PresenterAdapter<T>) {

    /**
     * Used for inflating the view holder layout.

     * @return Layout Resource Id.
     */
    protected abstract val layout: Int

    /**
     * Creates a new ViewHolder and inflates the view. Gets called only when the [RecyclerView] creates a new view.
     * Cached views go directly to [.bindViewHolder]

     * @param parent Parent ViewGroup used to set layout parameter.
     * *
     * @return new [VH].
     */
    @CallSuper
    protected fun onCreateViewHolder(parent: ViewGroup): VH {
        return createViewHolder(layout, parent)
    }

    /**
     * Creates a new ViewHolder and inflates the view.

     * @param layout Passed [.getLayout].
     * *
     * @param parent Parent ViewGroup used to set layout parameter.
     * *
     * @return new [VH].
     */
    protected fun createViewHolder(@LayoutRes layout: Int, parent: ViewGroup): VH {
    }

    /**
     * Gets [T] at adapter position.

     * @param position Position of [T] in adapter.
     * *
     * @return [T].
     */
    operator fun get(position: Int): T {
        return presenterAdapter[position]
    }

    /**
     * Binds [T] to [VH]. Use [.get] to retrieve neighbour [T].

     * @param viewHolder Current [VH].
     * *
     * @param item       Current [T]
     * *
     * @param position   Adapter position.
     */
    abstract fun  bindViewHolder(viewHolder: VH, item: T, position: Int)

    /**
     * Used for logging purposes.
     */
    fun tag(): String {
        return javaClass.simpleName
    }
}

fun <T> cloneWhenGreater(list: List<T>, threshold: T): List<T>
        where T : Comparable,
T : Cloneable {
    return list.filter { it > threshold }.map { it.clone() }
}