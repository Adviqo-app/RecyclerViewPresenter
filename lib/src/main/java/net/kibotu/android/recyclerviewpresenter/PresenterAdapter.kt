package net.kibotu.android.recyclerviewpresenter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.Constructor
import java.util.*


/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

open class PresenterAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Actual data containing [T] and it's [Presenter] type.
     */
    val data: ArrayList<Pair<T, Class<*>>>

    /**
     * Reference to the [OnItemClickListener].
     */

    var onItemClickListener: OnItemClickListener<T>? = null

    /**
     * Reference to the [OnItemFocusChangeListener].
     */
    var onItemFocusChangeListener: OnItemFocusChangeListener<T>? = null

    /**
     * Reference to the [View.OnKeyListener].
     */
    var onKeyListener: View.OnKeyListener? = null

    /**
     * List of allocated concrete implementation and used [Presenter].
     */
    protected var binderType: MutableList<Presenter<T, RecyclerView.ViewHolder>>

    /**
     * Reference to the bound [RecyclerView].
     */
    var recyclerView: RecyclerView? = null

    /**
     * Constructs the Adapter.
     */
    init {
        this.data = ArrayList<Pair<T, Class<*>>>()
        this.binderType = ArrayList<Presenter<T, RecyclerView.ViewHolder>>()
    }

    /**
     * {@inheritDoc}
     *
     *
     *
     * Except this time we use viewType to take the concrete implementation of the [Presenter].
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return getDataBinder(viewType).onCreateViewHolder(parent)
    }

    /**
     * {@inheritDoc}
     *
     *
     *
     * We take Also calls [IBaseViewHolder.onBindViewHolder].
     */
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is IBaseViewHolder)
            viewHolder.onBindViewHolder()
        getPresenterAt(position).bindViewHolder(viewHolder, get(position), position)
    }

    /**
     * Same as [.add] except it adds at a specific index.

     * @param index Adapter position.
     */
    fun add(index: Int, t: T, clazz: Class<*>) {
        data.add(index, Pair(t, clazz))
        addIfNotExists(clazz)
    }

    /**
     * Adds [T] at the end of the adapter list and linking a concrete Presenter

     * @param t     [T] as model.
     * *
     * @param clazz Concrete [Presenter] type.
     */
    fun add(t: T, clazz: Class<*>) {
        data.add(Pair(t, clazz))
        addIfNotExists(clazz)
    }

    /**
     * Allocates a concrete [Presenter] and adds it to the list once.

     * @param clazz Concrete [Presenter] representing the view type.
     */
    protected fun addIfNotExists(clazz: Class<*>) {
        this.binderType
                .filter { ClassExtensions.equals(it.javaClass, clazz) }
                .forEach { return }

        val constructor = clazz.constructors[0] as Constructor<T>
        var instance: Presenter<T, out RecyclerView.ViewHolder>? = null
        try {
            instance = constructor.newInstance(this) as Presenter<T, RecyclerView.ViewHolder>
            binderType.add(instance)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (instance == null)
            throw IllegalArgumentException(clazz.canonicalName + " has no constructor with parameter: " + javaClass.canonicalName)
    }

    /**
     * Returns [T] at adapter position.

     * @param position Adapter position.
     * *
     * @return [T]
     */
    operator fun get(position: Int): T {
        return data[position].first
    }

    /**
     * {@inheritDoc}
     *
     *
     * Returns position of concrete [Presenter] at adapter position.
     *

     * @param position Adapter position.
     * *
     * @return [Presenter] position. Returns `-1` if there is none to be found.
     */
    override fun getItemViewType(position: Int): Int {
        return binderType.indices.firstOrNull { ClassExtensions.equals(data[position].second, binderType[it].javaClass) }
                ?: -1
    }

    /**
     * Returns a concrete [Presenter] based on view type.

     * @param viewType [.getItemViewType]
     * *
     * @return Concrete [Presenter].
     */
    protected fun getDataBinder(viewType: Int): Presenter<T, RecyclerView.ViewHolder> {
        return binderType[viewType]
    }

    /**
     * Returns the position of the concrete [Presenter] at adapter position.

     * @param position Adapter position.
     * *
     * @return [Presenter]
     */
    protected fun getPresenterAt(position: Int): Presenter<T, RecyclerView.ViewHolder> {
        return binderType[getItemViewType(position)]
    }

    /**
     * {@inheritDoc}
     */
    override fun getItemCount(): Int {
        return data.size
    }


    /**
     * Clears the adapter and also removes cached views.
     * This is necessary otherwise different layouts will explode if you try to bind them to the wrong [RecyclerView.ViewHolder].
     */
    fun clear() {
        binderType.clear()
        data.clear()
        removeAllViews()
        notifyDataSetChanged()
    }

    /**
     * Removes all Views from the [RecyclerView].
     */
    fun removeAllViews() {
        if (recyclerView != null)
            recyclerView!!.removeAllViews()
    }

    /**
     * Used for logging purposes.
     */
    fun tag(): String {
        return javaClass.simpleName
    }

    /**
     * {@inheritDoc}
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
    }

    /**
     * {@inheritDoc}
     */
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView?) {
        this.recyclerView = recyclerView
    }

    /**
     * {@inheritDoc}
     *
     * Also calls [IBaseViewHolder.onBindViewHolder].
     */
    override fun onViewDetachedFromWindow(viewHolder: RecyclerView.ViewHolder?) {
        super.onViewDetachedFromWindow(viewHolder)
        if (viewHolder is IBaseViewHolder)
            viewHolder.onViewDetachedFromWindow()
    }

    /**
     * Retrieves [T] by using a [<]

     * @param item       [T]
     * *
     * @param comparator Filter criteria.
     * *
     * @return First [T].
     */
    fun getItemByComparator(item: T?, comparator: Comparator<T>): T? {
        for (i in 0..itemCount - 1)
            if (comparator.compare(get(i), item) == 0)
                return get(i)
        return null
    }

    /**
     * Returns  if adapter contains [T]

     * @param item [T]
     * *
     * @return `true` if contained.
     */
    operator fun contains(item: T?): Boolean {
        for (i in 0..itemCount - 1)
            if (get(i) == item)
                return true
        return false
    }

    /**
     * Returns  if adapter contains [T]

     * @param item [T]
     * *
     * @return `true` if contained.
     */
    fun contains(item: T?, comparator: Comparator<T>): Boolean {
        return (0..itemCount - 1).any { comparator.compare(get(it), item) == 0 }
    }

    /**
     * Returns adapter position of [T].

     * @param item [T]
     * *
     * @return `-1` if not contained.
     */
    fun position(item: T?): Int {
        return (0..itemCount - 1).firstOrNull { get(it) == item }
                ?: -1
    }

    /**
     * Returns adapter position of [T].

     * @param item       [T]
     * *
     * @param comparator Filter criteria.
     * *
     * @return `-1` if not contained.
     */
    fun position(item: T?, comparator: Comparator<T>): Int {
        return (0..itemCount - 1).firstOrNull { comparator.compare(get(it), item) == 0 }
                ?: -1
    }

    /**
     * Updates a model [T] at adapter position.

     * @param position Adapter position.
     * *
     * @param item     [T]
     */
    fun update(position: Int, item: T?) {
        if (item == null)
            return

        data[position] = Pair(item, data[position].second)
        notifyItemChanged(position)
    }

    /**
     * Remove an item at adapter position.

     * @param position Adapter position.
     */
    fun remove(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    /**
     * Sorting data.

     * @param comparator - Criteria for sorting.
     */
    fun sortBy(comparator: Comparator<T>) {
        Collections.sort(data) { left, right -> comparator.compare(left.first, right.first) }
    }

    companion object {

        /**
         * Default [Collections.sort] sorting.

         * @param adapter - Adapter where [T] requires to be implementing [Comparable]
         * *
         * @param <T>     - Model.
        </T> */
        fun <T : Comparable<Any>> sort(adapter: PresenterAdapter<T>) {
            Collections.sort(adapter.data) { o1, o2 -> o1.first.compareTo(o2.first) }
        }
    }
}