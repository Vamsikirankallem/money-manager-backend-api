package com.vamsi.MoneyManagerApp.repository;

import com.vamsi.MoneyManagerApp.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity,Long> {

    //select * from tbl_categories where profile_id = ?1
    List<CategoryEntity> findByProfileId(Long profile_id);

    CategoryEntity findByName(String name);

    //select * from tbl_categories where id = ?1 and profile_id = ?2
    Optional<CategoryEntity> findByIdAndProfileId(Long id, Long profile_id);

    //select * from tbl_categories where type = ?1 and profile_id = ?2
    List<CategoryEntity> findByTypeAndProfileId(String type,Long profile_id);

    Boolean existsByNameAndProfileId(String name,Long id);
}
