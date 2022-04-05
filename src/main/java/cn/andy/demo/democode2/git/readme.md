# 撤销提交
一种常见的场景是，提交代码以后，你突然意识到这个提交有问题，应该撤销掉，这时执行下面的命令就可以了。
```text
$ git revert HEAD
```
上面命令的原理是，在当前提交后面，新增一次提交，抵消掉上一次提交导致的所有变化。它不会改变过去的历史，所以是首选方式，没有任何丢失代码的风险。
**git revert**命令只能抵消上一个提交，如果想抵消多个提交，必须在命令行依次指定这些提交。比如，抵消前两个提交，要像下面这样写。
```text
$ git revert [倒数第一个提交] [倒数第二个提交]
```


如果撤销的提交是一个merge commit，此时在执行命令时会有些不一样。命令如下
git revert -m 1|2 parentCommitId

merge commit 包含两个 parent commit，代表该 merge commit 是从哪两个 commit 合并过来的。parentCommitId标识的就是“主线”，主线的内容将会保留。
通过以下命令可以查看merge commit代表的parent commit信息
git show mergeCommitId

-m 选项接收的参数是一个数字，数字取值为 1 和 2，也就是 Merge 行里面列出来的第一个还是第二个。




**git revert** 命令还有两个参数
* **--no-edit** ： 执行时不打开默认编辑器，直接使用 Git 自动生成的提交信息。
* **--no-commit** ：只抵消暂存区和工作区的文件变化，不产生新的提交。

# 丢弃提交
如果希望以前的提交在历史中彻底消失，而不是被抵消掉，可以使用 git reset 命令，丢弃掉某个提交之后的所有提交。
```text
git reset [last good SHA]
```
**git reset**的原理是，让最新提交的指针回到以前某个时点，该时点之后的提交都从历史中消失。
默认情况下，**git reset** 不改变工作区的文件（但会改变暂存区），--hard 参数可以让工作区里面的文件也回到以前的状态。
```text
git reset --hard [last good SHA]
```
执行 **git reset** 命令之后，如果想找回那些丢弃掉的提交，可以使用 git reflog 命令，具体做法参考这里。不过，这种做法有时效性，时间长了可能找不回来。


如果是已经推送到远端的commit，则还需要进行远程仓库强制更新

```text
git push \-f
```

需要注意的是：
如果回滚的commit是已经推送到远端的，则在idea日志显示的时候，此时commitId的信息不会被删除，只是HEAD会被移动到回滚点的commitId，
如果是还没有推送到远端的commitId，日志显示的信息会被删除，不会显示出来





#替换上一次的提交

提交以后，发现提交信息写错了，这时可以使用 git commit 命令的 --amend  参数，可以修改上一次的提交信息。

```text
git commit --amend -m "Fixes bug #42"
```
它的原理是产生一个新的提交对象，替换掉上一次提交产生的提交对象。

这时如果暂存区有发生变化的文件，会一起提交到仓库。所以，**--amend** 不仅可以修改提交信息，还可以整个把上一次提交替换掉。


# 撤销工作区的文件修改
如果工作区的某个文件被改乱了，但还没有提交，可以用 git checkout 命令找回本次修改之前的文件。

```text
git checkout -- [filename]
```
它的原理是先找暂存区，如果该文件有暂存的版本，则恢复该版本，否则恢复上一次提交的版本。

```text
注意，工作区的文件变化一旦被撤销，就无法找回了。
```

还可以使用以下命令
```text
git restore file
```



# 从暂存区撤销文件
如果不小心把一个文件添加到暂存区，可以用下面的命令撤销
```text
git rm --cached [filename]
```
上面的命令不影响已经提交的内容。
这个命令会把当前暂存区的文件删除，使其变成一个未被git管理的文件状态
，如果仅仅是让文件从暂存区撤出可以使用以下命令
```text
git restore --staged [filename]
```

# 撤销当前分支的变化

```text
# 新建一个 feature 分支，指向当前最新的提交
# 注意，这时依然停留在当前分支
$ git branch feature

# 切换到这几次提交之前的状态
$ git reset --hard [当前分支此前的最后一次提交]

# 切换到 feature 分支
$ git checkout feature

```

上面的操作等于是撤销当前分支的变化，将这些变化放到一个新建的分支。



# git log 与 git reflog命令
## git log
可以显示所有提交过的版本信息
## git reflog
可以查看所有分支的所有操作记录（包括已经被删除的 commit 记录和 reset 的操作）





# 将暂存区的文件撤销
如果我们git add <File>了一个文件后，此文件将进入暂存区，如果想要把它从暂存区撤出可以执行如下命令
```text
git restore --staged <File> 
```
上述git的命令就是将文件从暂存区撤出，但不会撤销文件的更改

如果我们想要修改了一个文件后，想要恢复原样，我们可以执行如下命令
```text
git restore <File>
```
上述git的命令就是将不在暂存区的文件撤销更改

