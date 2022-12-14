package me.yuugiri.hutil.processor.hook.point

import me.yuugiri.hutil.obfuscation.AbstractObfuscationMap
import me.yuugiri.hutil.processor.hook.EnumPointType
import me.yuugiri.hutil.processor.hook.HookInsnPoint
import me.yuugiri.hutil.processor.hook.IHookInsnPoint
import me.yuugiri.hutil.util.isReturnNode
import org.objectweb.asm.tree.AbstractInsnNode
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodNode

/**
 * inject hook at last [AbstractInsnNode] for return, do not use [EnumHookShift.AFTER] unless you want a hook that never been triggered
 */
class HookPointTail : IHookPoint {

    override fun hookPoints(obfuscationMap: AbstractObfuscationMap?, klass: ClassNode, method: MethodNode): List<IHookInsnPoint> {
        // find last return node
        method.instructions.reversed().forEach {
            if (isReturnNode(it)) {
                return listOf(HookInsnPoint(it, EnumPointType.RETURN))
            }
        }
        return emptyList()
    }
}