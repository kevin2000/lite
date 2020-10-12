package com.lite.core.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author joe
 * @since 2020-10-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SysMenuPerm implements Serializable {

    private static final long serialVersionUID=1L;

    private Long id;

    private Long menuId;

    private Long permId;


}
