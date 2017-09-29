package com.teamacronymcoders.contenttweaker.modules.materials;

import com.teamacronymcoders.base.materialsystem.MaterialSystem;
import com.teamacronymcoders.base.materialsystem.materialparts.MaterialPart;
import com.teamacronymcoders.contenttweaker.api.ctobjects.blockstate.ICTBlockState;
import com.teamacronymcoders.contenttweaker.api.ctobjects.blockstate.MCBlockState;
import com.teamacronymcoders.contenttweaker.modules.materials.materialparts.CTMaterialPart;
import com.teamacronymcoders.contenttweaker.modules.materials.materialparts.IMaterialPart;
import com.teamacronymcoders.contenttweaker.modules.vanilla.resources.BlockBracketHandler;
import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.BracketHandler;
import crafttweaker.mc1120.block.MCSpecificBlock;
import crafttweaker.zenscript.IBracketHandler;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import stanhebben.zenscript.compiler.IEnvironmentGlobal;
import stanhebben.zenscript.expression.ExpressionCallStatic;
import stanhebben.zenscript.expression.ExpressionInt;
import stanhebben.zenscript.expression.ExpressionString;
import stanhebben.zenscript.expression.partial.IPartialExpression;
import stanhebben.zenscript.parser.Token;
import stanhebben.zenscript.symbols.IZenSymbol;
import stanhebben.zenscript.type.ZenType;
import stanhebben.zenscript.type.natives.IJavaMethod;
import stanhebben.zenscript.util.ZenPosition;

import java.util.List;
import java.util.stream.Collectors;

@BracketHandler
public class MaterialPartBracketHandler implements IBracketHandler {
    private final IJavaMethod method;

    public MaterialPartBracketHandler() {
        method = CraftTweakerAPI.getJavaMethod(MaterialPartBracketHandler.class, "geMaterialPart", String.class);
    }

    public static IMaterialPart getMaterialPart(String name) {
        //MaterialPart materialPart = MaterialSystem.getMaterialPart(name);
        IMaterialPart zenMaterialPart = null;
        if (materialPart != null) {
            zenMaterialPart = new CTMaterialPart(materialPart);
        }
        return zenMaterialPart;
    }

    @Override
    public IZenSymbol resolve(IEnvironmentGlobal environment, List<Token> tokens) {
        IZenSymbol zenSymbol = null;

        if (tokens.size() == 5) {
            if ("materialpart".equalsIgnoreCase(tokens.get(0).getValue())) {
                String partName = String.join("", tokens.subList(2, 4)
                        .stream()
                        .map(Token::getValue)
                        .collect(Collectors.toList())
                        .toArray(new String[0]));
                zenSymbol = new MaterialPartBracketHandler.MaterialPartReferenceSymbol(environment, partName);
            }
        }

        return zenSymbol;
    }

    private class MaterialPartReferenceSymbol implements IZenSymbol {

        private final IEnvironmentGlobal environment;
        private final String name;

        public MaterialPartReferenceSymbol(IEnvironmentGlobal environment, String name) {
            this.environment = environment;
            this.name = name;
        }

        @Override
        public IPartialExpression instance(ZenPosition position) {
            return new ExpressionCallStatic(position, environment, method, new ExpressionString(position, name));
        }
    }
}
