package com.example.englishdiary.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.englishdiary.databinding.CorrectionResultItemBinding
import com.example.englishdiary.domain.model.CorrectionResult

class CorrectionResultItemAdapter
    : ListAdapter<CorrectionResult,
        CorrectionResultItemAdapter.CorrectionResultItemViewHolder>(DiffCallback) {

    class CorrectionResultItemViewHolder(var binding: CorrectionResultItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(correctionResult: CorrectionResult) {
            binding.jaText.text = correctionResult.jaText
            binding.correctedEnText.text = correctionResult.correctedEnText
            binding.reasonForCorrection.text = correctionResult.reasonForCorrection
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CorrectionResultItemViewHolder {
        return CorrectionResultItemViewHolder(
            CorrectionResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CorrectionResultItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CorrectionResult>() {
        override fun areItemsTheSame(
            oldItem: CorrectionResult,
            newItem: CorrectionResult
        ): Boolean {
            return oldItem.jaText == newItem.jaText
        }

        override fun areContentsTheSame(
            oldItem: CorrectionResult,
            newItem: CorrectionResult
        ): Boolean {
            return oldItem == newItem
        }
    }
}