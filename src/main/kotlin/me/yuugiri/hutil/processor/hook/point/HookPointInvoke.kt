package me.yuugiri.hutil.processor.hook.point

import me.yuugiri.hutil.obfuscation.AbstractObfuscationMap
import me.yuugiri.hutil.processor.hook.HookInsnPoint
import me.yuugiri.hutil.processor.hook.IHookInsnPoint
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.MethodInsnNode
import org.objectweb.asm.tree.MethodNode

/**
 * inject hook at every matched [MethodInsnNode]
 */
class HookPointInvoke(val matcher: IHookPointMatcher, val superclass: String = "") : IHookPoint {

    override fun hookPoints(obfuscationMap: AbstractObfuscationMap?, klass: ClassNode, method: MethodNode): List<IHookInsnPoint> {
        val nodes = mutableListOf<IHookInsnPoint>()

        method.instructions.forEach {
            if (it is MethodInsnNode) {
                var id = "${it.owner};${it.name}${it.desc}"
                if (!matcher.matches(id)) {
                    val supercl = if(superclass.isNotEmpty()) {
                        AbstractObfuscationMap.classObfuscationRecordReverse(obfuscationMap, superclass).obfuscatedName
                    } else it.owner
                    val obf = AbstractObfuscationMap.methodObfuscationRecord(obfuscationMap, supercl, it.name, it.desc)
                    id = "${AbstractObfuscationMap.classObfuscationRecord(obfuscationMap, it.owner).name};${obf.name}${obf.description}"
                    if (!matcher.matches(id)) {
                        return@forEach
                    }
                }
                nodes.add(HookInsnPoint(it))
            }
        }

        return nodes
    }
}