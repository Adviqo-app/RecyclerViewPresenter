package net.kibotu.android.recyclerviewpresenter

/**
 * Created by [Jan Rabe](https://about.me/janrabe).
 */

class ViewModel<T> {

    var model: T? = null
    var onItemClickListener: OnItemClickListener<ViewModel<T>>? = null

    fun setModel(model: T): ViewModel<T> {
        this.model = model
        return this
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener<ViewModel<T>>): ViewModel<T> {
        this.onItemClickListener = onItemClickListener
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as ViewModel<*>

        if (model != other.model) return false
        if (onItemClickListener != other.onItemClickListener) return false

        return true
    }

    override fun hashCode(): Int {
        var result = model?.hashCode() ?: 0
        result = 31 * result + (onItemClickListener?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "ViewModel(model=$model, onItemClickListener=$onItemClickListener)"
    }
}