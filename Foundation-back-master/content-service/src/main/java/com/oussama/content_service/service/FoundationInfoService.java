package com.oussama.content_service.service;

import com.oussama.content_service.Dto.FoundationInfoDto;
import com.oussama.content_service.entity.FoundationInfo;
import com.oussama.content_service.enums.InfoType;
import com.oussama.content_service.mapper.ContentMapper;
import com.oussama.content_service.repository.FoundationInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FoundationInfoService {

    private final FoundationInfoRepository foundationInfoRepository;
    private final ContentMapper contentMapper;

    public FoundationInfoDto createFoundationInfo(FoundationInfoDto foundationInfoDto) {
        log.info("Creating foundation info: {}", foundationInfoDto.getTitle());

        // Utiliser la méthode de base du mapper
        FoundationInfo foundationInfo = contentMapper.toFoundationInfoEntity(foundationInfoDto);

        // Définir manuellement les champs système
        foundationInfo.setId(null); // Assurer que l'ID est null pour la création
        foundationInfo.setCreatedAt(null); // Sera défini automatiquement
        foundationInfo.setUpdatedAt(null); // Sera défini automatiquement

        // Assigner un ordre d'affichage automatique si non spécifié
        if (foundationInfo.getDisplayOrder() == null) {
            int maxOrder = foundationInfoRepository.findMaxDisplayOrderByType(foundationInfo.getType())
                    .orElse(0);
            foundationInfo.setDisplayOrder(maxOrder + 1);
        }

        foundationInfo = foundationInfoRepository.save(foundationInfo);
        log.info("Foundation info created with ID: {}", foundationInfo.getId());

        return contentMapper.toFoundationInfoDto(foundationInfo);
    }

    public FoundationInfoDto updateFoundationInfo(Long id, FoundationInfoDto foundationInfoDto) {
        log.info("Updating foundation info with ID: {}", id);

        FoundationInfo foundationInfo = foundationInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foundation info not found with ID: " + id));

        // Mise à jour manuelle des champs modifiables seulement
        if (foundationInfoDto.getTitle() != null) {
            foundationInfo.setTitle(foundationInfoDto.getTitle());
        }
        if (foundationInfoDto.getContent() != null) {
            foundationInfo.setContent(foundationInfoDto.getContent());
        }
        if (foundationInfoDto.getType() != null) {
            foundationInfo.setType(foundationInfoDto.getType());
        }
        if (foundationInfoDto.getDisplayOrder() != null) {
            foundationInfo.setDisplayOrder(foundationInfoDto.getDisplayOrder());
        }
        if (foundationInfoDto.getIsActive() != null) {
            foundationInfo.setIsActive(foundationInfoDto.getIsActive());
        }

        // Ne pas mettre à jour : id, createdAt, updatedAt

        foundationInfo = foundationInfoRepository.save(foundationInfo);

        log.info("Foundation info updated successfully: {}", id);
        return contentMapper.toFoundationInfoDto(foundationInfo);
    }

    @Transactional(readOnly = true)
    public FoundationInfoDto getFoundationInfoById(Long id) {
        FoundationInfo foundationInfo = foundationInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foundation info not found with ID: " + id));

        return contentMapper.toFoundationInfoDto(foundationInfo);
    }

    @Transactional(readOnly = true)
    public List<FoundationInfoDto> getAllActiveFoundationInfo() {
        return foundationInfoRepository.findByIsActiveTrueOrderByDisplayOrderAsc()
                .stream()
                .map(contentMapper::toFoundationInfoDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<FoundationInfoDto> getFoundationInfoByType(InfoType type) {
        return foundationInfoRepository.findByTypeAndIsActiveTrue(type)
                .map(contentMapper::toFoundationInfoDto);
    }

    @Transactional(readOnly = true)
    public List<FoundationInfoDto> getFoundationInfoByTypes(List<InfoType> types) {
        return foundationInfoRepository.findByTypeInAndIsActiveTrueOrderByDisplayOrderAsc(types)
                .stream()
                .map(contentMapper::toFoundationInfoDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<FoundationInfoDto> getAllFoundationInfo() {
        return foundationInfoRepository.findAllByOrderByTypeAscDisplayOrderAsc()
                .stream()
                .map(contentMapper::toFoundationInfoDto)
                .toList();
    }

    public FoundationInfoDto activateFoundationInfo(Long id) {
        log.info("Activating foundation info with ID: {}", id);

        FoundationInfo foundationInfo = foundationInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foundation info not found with ID: " + id));

        foundationInfo.setIsActive(true);
        foundationInfo = foundationInfoRepository.save(foundationInfo);

        log.info("Foundation info activated successfully: {}", id);
        return contentMapper.toFoundationInfoDto(foundationInfo);
    }

    public FoundationInfoDto deactivateFoundationInfo(Long id) {
        log.info("Deactivating foundation info with ID: {}", id);

        FoundationInfo foundationInfo = foundationInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foundation info not found with ID: " + id));

        foundationInfo.setIsActive(false);
        foundationInfo = foundationInfoRepository.save(foundationInfo);

        log.info("Foundation info deactivated successfully: {}", id);
        return contentMapper.toFoundationInfoDto(foundationInfo);
    }

    public void reorderFoundationInfo(List<Long> orderedIds) {
        log.info("Reordering foundation info items");

        for (int i = 0; i < orderedIds.size(); i++) {
            Long id = orderedIds.get(i);
            FoundationInfo foundationInfo = foundationInfoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Foundation info not found with ID: " + id));

            foundationInfo.setDisplayOrder(i + 1);
            foundationInfoRepository.save(foundationInfo);
        }

        log.info("Foundation info items reordered successfully");
    }

    public void deleteFoundationInfo(Long id) {
        log.info("Deleting foundation info with ID: {}", id);

        FoundationInfo foundationInfo = foundationInfoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Foundation info not found with ID: " + id));

        foundationInfoRepository.delete(foundationInfo);
        log.info("Foundation info deleted successfully: {}", id);
    }
}

