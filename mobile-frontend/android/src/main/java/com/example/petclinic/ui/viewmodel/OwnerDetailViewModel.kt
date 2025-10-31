package com.example.petclinic.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petclinic.data.model.Owner
import com.example.petclinic.data.model.OwnerRequest
import com.example.petclinic.data.model.Pet
import com.example.petclinic.data.model.PetRequest
import com.example.petclinic.data.model.PetType
import com.example.petclinic.data.network.ApiResult
import com.example.petclinic.data.repository.PetClinicRepository
import kotlinx.coroutines.launch

class OwnerDetailViewModel(
    private val repository: PetClinicRepository = PetClinicRepository()
) : ViewModel() {
    
    companion object {
        private const val TAG = "OwnerDetailViewModel"
    }
    
    var ownerState by mutableStateOf<ApiResult<Owner>?>(null)
        private set
    
    var petTypesState by mutableStateOf<ApiResult<List<PetType>>?>(null)
        private set
    
    init {
        loadPetTypes()
    }
    
    fun loadOwnerWithVisits(ownerId: Int) {
        viewModelScope.launch {
            repository.getOwnerWithVisits(ownerId).collect { result ->
                ownerState = result
            }
        }
    }
    
    private fun loadPetTypes() {
        viewModelScope.launch {
            repository.getPetTypes().collect { result ->
                petTypesState = result
            }
        }
    }
    
    fun updateOwner(
        ownerId: Int, 
        ownerRequest: OwnerRequest, 
        onSuccess: () -> Unit, 
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Updating owner $ownerId with data: $ownerRequest")
            when (val result = repository.updateOwner(ownerId, ownerRequest)) {
                is ApiResult.Success -> {
                    Log.d(TAG, "Owner $ownerId updated successfully")
                    onSuccess()
                    loadOwnerWithVisits(ownerId) // Refresh the owner data
                }
                is ApiResult.Error -> {
                    Log.e(TAG, "Failed to update owner $ownerId: ${result.exception.message}", result.exception)
                    onError(result.exception.message ?: "Unknown error")
                }
                is ApiResult.Loading -> {
                    Log.d(TAG, "Update owner $ownerId - loading state")
                }
            }
        }
    }
    
    fun addPet(
        ownerId: Int,
        petRequest: PetRequest,
        onSuccess: (Pet) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Adding pet for owner $ownerId: $petRequest")
            when (val result = repository.addPet(ownerId, petRequest)) {
                is ApiResult.Success -> {
                    Log.d(TAG, "Pet added successfully for owner $ownerId")
                    onSuccess(result.data)
                    loadOwnerWithVisits(ownerId) // Refresh the owner data
                }
                is ApiResult.Error -> {
                    Log.e(TAG, "Failed to add pet for owner $ownerId: ${result.exception.message}", result.exception)
                    onError(result.exception.message ?: "Unknown error")
                }
                is ApiResult.Loading -> {
                    Log.d(TAG, "Add pet for owner $ownerId - loading state")
                }
            }
        }
    }
}
