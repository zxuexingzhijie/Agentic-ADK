#!/bin/bash

# 有道翻译引擎依赖安装脚本
echo "正在安装有道翻译引擎依赖..."

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "错误: Maven未安装，请先安装Maven"
    exit 1
fi

# 安装依赖
echo "正在下载Maven依赖..."
mvn clean dependency:tree > tree.txt

if [ $? -eq 0 ]; then
    echo "有道翻译引擎依赖安装完成！"
else
    echo "错误: 依赖安装失败"
    exit 1
fi 