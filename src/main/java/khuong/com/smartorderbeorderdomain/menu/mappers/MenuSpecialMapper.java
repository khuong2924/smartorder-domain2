package khuong.com.smartorderbeorderdomain.menu.mappers;

@Mapper(componentModel = "spring", uses = {MenuItemMapper.class})
public interface MenuSpecialMapper {
    @Mapping(target = "items", source = "items")
    MenuSpecialDTO toDTO(MenuSpecial special);

    @Mapping(target = "items", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    MenuSpecial toEntity(MenuSpecialDTO dto);

    List<MenuSpecialDTO> toDTOList(List<MenuSpecial> specials);
}