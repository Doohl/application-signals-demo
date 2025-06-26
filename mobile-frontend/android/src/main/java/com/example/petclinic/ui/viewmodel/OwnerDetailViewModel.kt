package com.example.petclinic.ui.viewmodel

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
            when (val result = repository.updateOwner(ownerId, ownerRequest)) {
                is ApiResult.Success -> {
                    onSuccess()
                    loadOwnerWithVisits(ownerId) // Refresh the owner data
                }
                is ApiResult.Error -> {
                    onError(result.exception.message ?: "Unknown error")
                }
                is ApiResult.Loading -> {
                    // Handle loading state if needed
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
            when (val result = repository.addPet(ownerId, petRequest)) {
                is ApiResult.Success -> {
                    onSuccess(result.data)
                    loadOwnerWithVisits(ownerId) // Refresh the owner data
                }
                is ApiResult.Error -> {
                    onError(result.exception.message ?: "Unknown error")
                }
                is ApiResult.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }
}
